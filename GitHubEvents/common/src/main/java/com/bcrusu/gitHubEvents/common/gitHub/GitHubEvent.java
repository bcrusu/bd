package com.bcrusu.gitHubEvents.common.gitHub;

import com.bcrusu.gitHubEvents.common.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;

public class GitHubEvent {
    public String id;
    public String type;
    public Long createdAt;    // UTC
    public String repositoryName;
    public String actorLogin;
    public String organisationLogin;

    private GitHubEvent() {
    }


    public static GitHubEvent create(JsonNode jsonNode) {
        GitHubEvent result = new GitHubEvent();
        result.id = JsonUtils.getTextValue(jsonNode, "id");
        result.type = JsonUtils.getTextValue(jsonNode, "type");
        result.repositoryName = JsonUtils.getTextValue(jsonNode, "repo", "name");
        result.actorLogin = JsonUtils.getTextValue(jsonNode, "actor", "login");
        result.organisationLogin = JsonUtils.getTextValue(jsonNode, "org", "login");

        String createdAtStr = JsonUtils.getTextValue(jsonNode, "created_at");
        result.createdAt = createdAtStr != null ? Instant.parse(createdAtStr).getEpochSecond() : 0L;

        return result;
    }
}
