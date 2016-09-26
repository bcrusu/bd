package com.bcrusu.gitHubEvents.watcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GitHubEventSource {
    private final static String API_MEDIA_TYPE = "application/vnd.github.v3+json";
    private final static String HTTP_HEADER_X_POLL_INTERVAL = "X-Poll-Interval";

    private final String _oauthToken;
    private final String _url;
    private final int _pollInterval;
    private Observable<GitHubEvent> _eventSource = null;
    private ResponseInfo _lastResponseInfo = null;

    public GitHubEventSource(String oauthToken, String url, int pollInterval) {
        _oauthToken = oauthToken;
        _url = url;
        _pollInterval = pollInterval;
    }

    public GitHubEventSource(String oauthToken, String url) {
        this(oauthToken, url, 5);
    }

    public Observable<GitHubEvent> getEventsSource() {
        if (_eventSource == null) {
            _eventSource = createEventsSource();
        }

        return _eventSource;
    }

    private Observable<GitHubEvent> createEventsSource() {
        Observable<GitHubEvent> result = Observable.create(subscriber -> {
            // if the rate limit is exceeded do not call API
            if (isRateLimitExceeded()) {
                //TODO: throw exception and use retryWhen below to wait until the rate limit reset passes
                subscriber.onCompleted();
                return;
            }

            OkHttpClient client = new OkHttpClient();
            Request request = buildFirstPageRequest();

            try {
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {
                    //TODO: proper error
                    subscriber.onError(null);
                    return;
                }

                if (response.code() == HttpConstants.HTTP_STATUS_CODE_304_NOT_MODIFIED) {
                    //TODO: use X-Poll-Interval header
                    subscriber.onCompleted();
                    return;
                }

                ResponseInfo responseInfo = ResponseInfo.parse(response);

                //TODO: read events (all pages) and emit
                List<GitHubEvent> events = parseResponseBody(response.body());

                for (GitHubEvent event : events)
                    subscriber.onNext(event);

                _lastResponseInfo = responseInfo;

            } catch (IOException e) {
                //TODO: log
            }

            subscriber.onCompleted();
        });

        return result
                .retryWhen(errors -> errors.flatMap(error -> {
                    //TODO: pass wait time using 'onError' above
                    return Observable.timer(5, TimeUnit.SECONDS);
                }))
                .repeatWhen(source -> source.flatMap(x -> Observable.timer(_pollInterval, TimeUnit.SECONDS)));
    }

    private Request buildFirstPageRequest() {
        Request.Builder builder = createRequestBuilder(_url);

        if (_lastResponseInfo != null) {
            String eTag = _lastResponseInfo.getETag();
            if (eTag != null)
                builder.header(HttpConstants.HTTP_HEADER_IF_NONE_MATCH, eTag);

            String lastModified = _lastResponseInfo.getLastModified();
            if (lastModified != null)
                builder.header(HttpConstants.HTTP_HEADER_IF_MODIFIED_SINCE, lastModified);
        }

        return builder.build();
    }

    private Request buildNextPageRequest(String url) {
        Request.Builder builder = createRequestBuilder(url);
        return builder.build();
    }

    private Request.Builder createRequestBuilder(String url) {
        return new Request.Builder()
                .url(url)
                .header(HttpConstants.HTTP_HEADER_USER_AGENT, "minion007")
                .header(HttpConstants.HTTP_HEADER_ACCEPT, API_MEDIA_TYPE)
                .header(HttpConstants.HTTP_HEADER_AUTHORIZATION, "token " + _oauthToken);
    }

    private boolean isRateLimitExceeded() {
        // rate limits are returned by the API for each request
        // if this is the first API request there is nothing to check
        if (_lastResponseInfo == null)
            return false;

        RateLimit rateLimit = _lastResponseInfo.getRateLimit();
        if (rateLimit.getRemaining() > 0)
            return false;

        // check if the reset date has passed
        if (rateLimit.getReset().compareTo(Instant.now()) > 0)
            return false;

        return true;
    }

    private List<GitHubEvent> parseResponseBody(ResponseBody body) throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(body.byteStream());

        return StreamSupport.stream(jsonNode.spliterator(), false)
                .map(GitHubEvent::create)
                .collect(Collectors.toList());
    }
}
