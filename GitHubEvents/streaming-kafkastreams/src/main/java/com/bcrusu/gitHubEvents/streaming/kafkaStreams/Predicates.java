package com.bcrusu.gitHubEvents.streaming.kafkaStreams;

class Predicates {
    static <K, V> boolean keyNotNull(K key, V value) {
        return key != null;
    }

    static <K, V> boolean valueNotNull(K key, V value) {
        return value != null;
    }
}
