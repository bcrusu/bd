package com.bcrusu.gitHubEvents.common.cli;

public class CliPropertiesParseException extends RuntimeException {
    public CliPropertiesParseException() {
    }

    public CliPropertiesParseException(String message) {
        super(message);
    }

    public CliPropertiesParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CliPropertiesParseException(Throwable cause) {
        super(cause);
    }

    public CliPropertiesParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
