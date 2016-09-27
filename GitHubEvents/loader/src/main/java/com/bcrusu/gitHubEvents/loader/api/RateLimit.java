package com.bcrusu.gitHubEvents.loader.api;

import okhttp3.Response;

import java.time.Instant;

class RateLimit {
    private final static String HTTP_HEADER_X_RATE_LIMIT_LIMIT = "X-RateLimit-Limit";
    private final static String HTTP_HEADER_X_RATE_LIMIT_REMAINING = "X-RateLimit-Remaining";
    private final static String HTTP_HEADER_X_RATE_LIMIT_RESET = "X-RateLimit-Reset";

    private final long _limit;
    private final long _remaining;
    private final Instant _reset;

    private RateLimit(long limit, long remaining, Instant reset) {
        _limit = limit;
        _remaining = remaining;
        _reset = reset;
    }

    public long getLimit() {
        return _limit;
    }

    public long getRemaining() {
        return _remaining;
    }

    public Instant getReset() {
        return _reset;
    }

    static RateLimit parse(Response response) {
        long limit = tryParseLong(response.header(HTTP_HEADER_X_RATE_LIMIT_LIMIT), 5000);
        long remaining = tryParseLong(response.header(HTTP_HEADER_X_RATE_LIMIT_REMAINING), 5000);
        Instant reset;

        long resetSeconds = tryParseLong(response.header(HTTP_HEADER_X_RATE_LIMIT_RESET), -1);
        if (resetSeconds == -1) {
            // set to 1h in the future
            reset = Instant.now().plusSeconds(60 * 60);
        } else {
            reset = Instant.ofEpochSecond(resetSeconds);
        }

        return new RateLimit(limit, remaining, reset);
    }

    private static long tryParseLong(String value, long defaultValue) {
        if (value == null)
            return defaultValue;

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
