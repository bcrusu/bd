package com.bcrusu.gitHubEvents.common.kafka;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

public class CustomConsumerRebalanceListener implements ConsumerRebalanceListener {
    private final KafkaConsumer _consumer;
    private final boolean _seekToBeginning;

    public CustomConsumerRebalanceListener(KafkaConsumer consumer, boolean seekToBeginning) {
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