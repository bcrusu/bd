package com.bcrusu.gitHubEvents.loader.api;

import okhttp3.Response;

import java.util.HashMap;
import java.util.Map;

class Links {
    private final static String HTTP_HEADER_LINK = "Link";
    private final static String REL_NEXT = "next";
    private final static String REL_PREV = "prev";
    private final static String REL_FIRST = "first";
    private final static String REL_LAST = "last";

    private final Map<String, String> _linksMap;

    private Links(Map<String, String> linksMap) {
        _linksMap = linksMap;
    }

    public String getNext() {
        return _linksMap.get(REL_NEXT);
    }

    public String getPrev() {
        return _linksMap.get(REL_PREV);
    }

    public String getFirst() {
        return _linksMap.get(REL_FIRST);
    }

    public String getLast() {
        return _linksMap.get(REL_LAST);
    }

    static Links parse(Response response) {
        Map<String, String> linksMap = new HashMap<>();

        String link = response.header(HTTP_HEADER_LINK, null);
        if (link == null)
            return null;

        String[] linkSplits = link.split(",");
        for (String linkSplit : linkSplits) {
            String[] splits = linkSplit.split(";");

            // skip invalid links; valid format is: <url>; rel="xxx"
            if (splits.length != 2)
                continue;

            String url = splits[0].trim();
            if (!url.startsWith("<") || !url.endsWith(">"))
                continue;

            url = url.substring(1, url.length() - 1);

            String rel = splits[1].trim().toLowerCase();
            if (!rel.startsWith("rel=\"") || !rel.endsWith("\""))
                continue;

            rel = rel.substring(5, rel.length() - 1);

            linksMap.put(rel, url);
        }

        return new Links(linksMap);
    }
}
