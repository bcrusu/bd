package com.bcrusu.gitHubEvents.common.cli;

import java.util.Properties;

public class GitHubApiProperties extends CliProperties {
    private final static String PROPERTY_NAME_GITHUB_EVENTS_URL = "github.eventsUrl";
    private final static String PROPERTY_NAME_GITHUB_OAUTH2_TOKEN = "github.oauth2_token";
    private final static String PROPERTY_DEFAULT_GITHUB_EVENTS_URL = "https://api.github.com/events";

    public GitHubApiProperties(Properties properties) {
        super(properties);
    }

    @Override
    public boolean validate() {
        return hasValidStringProperty(PROPERTY_NAME_GITHUB_OAUTH2_TOKEN);
    }

    public String getEventsUrl() {
        return getStringProperty(PROPERTY_NAME_GITHUB_EVENTS_URL, PROPERTY_DEFAULT_GITHUB_EVENTS_URL);
    }

    public String getOauth2Token() {
        return getStringProperty(PROPERTY_NAME_GITHUB_OAUTH2_TOKEN);
    }
}
