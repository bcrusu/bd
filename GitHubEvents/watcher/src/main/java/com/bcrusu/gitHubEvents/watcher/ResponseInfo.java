package com.bcrusu.gitHubEvents.watcher;

import okhttp3.Response;

class ResponseInfo {
    private final RateLimit _rateLimit;
    private final Links _links;
    private final String _eTag;
    private final String _lastModified;

    private ResponseInfo(RateLimit rateLimit, Links links, String eTag, String lastModified) {
        _rateLimit = rateLimit;
        _links = links;
        _eTag = eTag;
        _lastModified = lastModified;
    }

    public String getETag() {
        return _eTag;
    }

    public String getLastModified() {
        return _lastModified;
    }

    public RateLimit getRateLimit() {
        return _rateLimit;
    }

    public Links getLinks() {
        return _links;
    }

    static ResponseInfo parse(Response response) {
        RateLimit rateLimit = RateLimit.parse(response);
        Links links = Links.parse(response);
        String eTag = response.header(HttpConstants.HTTP_HEADER_ETAG, null);
        String lastModified = response.header(HttpConstants.HTTP_HEADER_LAST_MODIFIED, null);

        return new ResponseInfo(rateLimit, links, eTag, lastModified);
    }
}
