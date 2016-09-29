package com.bcrusu.gitHubEvents.indexer;

public class IndexerError extends Error {
    public IndexerError() {
    }

    public IndexerError(String message) {
        super(message);
    }

    public IndexerError(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexerError(Throwable cause) {
        super(cause);
    }

    public IndexerError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
