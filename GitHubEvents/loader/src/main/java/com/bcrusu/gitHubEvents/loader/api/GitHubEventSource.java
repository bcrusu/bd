package com.bcrusu.gitHubEvents.loader.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GitHubEventSource {
    private static final Logger _logger = LoggerFactory.getLogger(GitHubEventSource.class);

    private final static String API_MEDIA_TYPE = "application/vnd.github.v3+json";
    private final static String HTTP_HEADER_X_POLL_INTERVAL = "X-Poll-Interval";

    private final String _oauthToken;
    private final String _url;
    private final int _pollInterval;
    private final OkHttpClient _client;

    private Observable<GitHubEvent> _observable = null;
    private RateLimit _lastRateLimit = null;
    private String _lastETag = null;
    private String _lastLastModified = null;
    private String _lastEventId = null;  // last seen event id

    private GitHubEventSource(String oauthToken, String url, int pollInterval) {
        _oauthToken = oauthToken;
        _url = url;
        _pollInterval = pollInterval;
        _client = new OkHttpClient();
    }

    public GitHubEventSource(String oauthToken, String url) {
        this(oauthToken, url, 5);
    }

    public Observable<GitHubEvent> getObservable() {
        if (_observable == null) {
            _observable = createObservable();
        }

        return _observable;
    }

    private Observable<GitHubEvent> createObservable() {
        Observable<GitHubEvent> result = Observable.create(subscriber -> {
            String url = _url;
            boolean isFirstPage = true;
            String eTag = _lastETag;
            String lastModified = _lastLastModified;

            LinkedList<GitHubEvent> toEmit = new LinkedList<>();
            Set<String> seenEventIds = new HashSet<>();  // some events are pushed to next page during consecutive fetches

            page_fetch_loop:
            while (true) {
                if (isRateLimitExceeded())
                    break;

                Response response = fetchPage(url, eTag, lastModified);
                if (response == null || !response.isSuccessful()) {
                    // TODO: retry logic (try multiple times to fetch the page before breaking the fetch loop)
                    break;
                }

                if (response.code() == HttpConstants.HTTP_STATUS_CODE_304_NOT_MODIFIED)
                    break;

                if (isFirstPage) {
                    _lastETag = response.header(HttpConstants.HTTP_HEADER_ETAG);
                    _lastLastModified = response.header(HttpConstants.HTTP_HEADER_LAST_MODIFIED);
                }

                _lastRateLimit = RateLimit.parse(response);
                _logger.info("GitHub API rate limit: limit={}, remaining={}", _lastRateLimit.getLimit(), _lastRateLimit.getRemaining());

                List<GitHubEvent> events = parseResponseBody(response.body());
                if (events == null)
                    break;

                for (GitHubEvent event : events) {
                    String eventId = event.getId();

                    // skip events from previous page
                    if (seenEventIds.contains(eventId))
                        continue;

                    // stop when reached the last fetched event
                    if (_lastEventId != null && _lastEventId.compareTo(eventId) == 0)
                        break page_fetch_loop;

                    toEmit.addFirst(event);
                    seenEventIds.add(eventId);
                }

                Links links = Links.parse(response);
                if (links == null || links.getNext() == null)
                    break;

                url = links.getNext();
                isFirstPage = false;
                eTag = null;
                lastModified = null;
            }

            if (toEmit.size() > 0) {
                // emit events
                toEmit.forEach(subscriber::onNext);

                // save last event id to be used on next poll operation
                _lastEventId = toEmit.getLast().getId();
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

    private Response fetchPage(String url, String eTag, String lastModified) {
        _logger.info("Fetching url {}", url);

        Request.Builder builder = createRequestBuilder(url);

        if (eTag != null)
            builder.header(HttpConstants.HTTP_HEADER_IF_NONE_MATCH, eTag);

        if (lastModified != null)
            builder.header(HttpConstants.HTTP_HEADER_IF_MODIFIED_SINCE, lastModified);

        Request request = builder.build();

        try {
            return _client.newCall(request).execute();
        } catch (IOException e) {
            _logger.error(String.format("Error fetching url {}", url), e);
            return null;
        }
    }

    private Request.Builder createRequestBuilder(String url) {
        return new Request.Builder()
                .url(url)
                .header(HttpConstants.HTTP_HEADER_USER_AGENT, "minion007")
                .header(HttpConstants.HTTP_HEADER_ACCEPT, API_MEDIA_TYPE)
                .header(HttpConstants.HTTP_HEADER_AUTHORIZATION, "token " + _oauthToken);
    }

    private boolean isRateLimitExceeded() {
        RateLimit rateLimit = _lastRateLimit;

        // rate limits are returned by the API for each request
        // if this is the first API request there is nothing to check
        if (rateLimit == null)
            return false;

        if (rateLimit.getRemaining() > 0)
            return false;

        // check if the reset date has passed
        if (rateLimit.getReset().compareTo(Instant.now()) > 0)
            return false;

        return true;
    }

    private List<GitHubEvent> parseResponseBody(ResponseBody body) {
        byte[] bodyBytes;
        try {
            bodyBytes = body.bytes();
        } catch (IOException e) {
            _logger.error("Error reading response body", e);
            return null;
        }

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(bodyBytes);

            return StreamSupport.stream(jsonNode.spliterator(), false)
                    .map(GitHubEvent::create)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            _logger.error("Error parsing response body", e);
            return null;
        }
    }
}
