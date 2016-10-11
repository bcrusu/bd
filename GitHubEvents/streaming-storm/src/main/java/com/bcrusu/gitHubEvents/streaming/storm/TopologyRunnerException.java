package com.bcrusu.gitHubEvents.streaming.storm;

public class TopologyRunnerException extends RuntimeException {
    public TopologyRunnerException() {
    }

    public TopologyRunnerException(String message) {
        super(message);
    }

    public TopologyRunnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TopologyRunnerException(Throwable cause) {
        super(cause);
    }

    public TopologyRunnerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
