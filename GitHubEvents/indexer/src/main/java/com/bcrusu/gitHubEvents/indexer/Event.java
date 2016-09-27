package com.bcrusu.gitHubEvents.indexer;

public class Event {
    private final String _id;
    private final String _json;

    public Event(String id, String json) {
        _id = id;
        _json = json;
    }

    public String getId() {
        return _id;
    }

    public String getJson() {
        return _json;
    }
}
