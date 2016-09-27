package com.bcrusu.gitHubEvents.loader.api;

import com.fasterxml.jackson.databind.JsonNode;

public class GitHubEvent {
    private final String _id;
    private final String _type;
    private final String _createdAt;
    private final String _json;

    private GitHubEvent(String id, String type, String createdAt, String json) {
        _id = id;
        _type = type;
        _createdAt = createdAt;
        _json = json;
    }

    public String getId() {
        return _id;
    }

    public String getType() {
        return _type;
    }

    public String getCreatedAt() {
        return _createdAt;
    }

    public String getJson() {
        return _json;
    }

    static GitHubEvent create(JsonNode jsonNode) {
        String id = jsonNode.get("id").textValue();
        String type = jsonNode.get("type").textValue();
        String createdAt = jsonNode.get("created_at").textValue();
        String json = jsonNode.toString();

        return new GitHubEvent(id, type, createdAt, json);
    }
}
