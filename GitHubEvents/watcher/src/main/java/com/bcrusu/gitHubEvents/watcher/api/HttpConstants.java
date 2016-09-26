package com.bcrusu.gitHubEvents.watcher.api;

class HttpConstants {
    final static String HTTP_HEADER_ETAG = "ETag";
    final static String HTTP_HEADER_LAST_MODIFIED = "Last-Modified";
    final static String HTTP_HEADER_IF_NONE_MATCH = "If-None-Match";
    final static String HTTP_HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
    final static String HTTP_HEADER_ACCEPT = "Accept";
    final static String HTTP_HEADER_AUTHORIZATION = "Authorization";
    final static String HTTP_HEADER_USER_AGENT = "User-Agent";

    final static int HTTP_STATUS_CODE_304_NOT_MODIFIED = 304;
}
