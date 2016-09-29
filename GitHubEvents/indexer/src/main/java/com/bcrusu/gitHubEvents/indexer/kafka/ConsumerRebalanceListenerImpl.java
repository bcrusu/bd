package com.bcrusu.gitHubEvents.indexer.kafka;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

public class ConsumerRebalanceListenerImpl implements ConsumerRebalanceListener {
    private final KafkaConsumer<String, String> _consumer;
    private final boolean _seekToBeginning;

    public ConsumerRebalanceListenerImpl(KafkaConsumer<String, String> consumer, boolean seekToBeginning) {
        _consumer = consumer;
        _seekToBeginning = seekToBeginning;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {

    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
        if (_seekToBeginning) {
            _consumer.seekToBeginning(collection);
        }
    }
}