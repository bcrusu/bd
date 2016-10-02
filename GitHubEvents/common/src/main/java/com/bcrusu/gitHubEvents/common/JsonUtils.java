package com.bcrusu.gitHubEvents.common;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonUtils {
    public static JsonNode getChildNode(JsonNode jsonNode, String... paths) {
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            if (!jsonNode.has(path))
                return null;

            jsonNode = jsonNode.get(path);
        }

        return jsonNode;
    }

    public static String getTextValue(JsonNode jsonNode, String... paths) {
        jsonNode = getChildNode(jsonNode, paths);
        return jsonNode != null ? jsonNode.textValue() : null;
    }

    public static Long getLongValue(JsonNode jsonNode, String... paths) {
        jsonNode = getChildNode(jsonNode, paths);
        return jsonNode != null ? jsonNode.longValue() : null;
    }
}
