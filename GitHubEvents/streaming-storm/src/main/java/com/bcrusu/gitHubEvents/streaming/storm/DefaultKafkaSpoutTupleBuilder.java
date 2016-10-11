package com.bcrusu.gitHubEvents.streaming.storm;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.storm.kafka.spout.KafkaSpoutTupleBuilder;
import org.apache.storm.tuple.Values;

import java.util.List;

class DefaultKafkaSpoutTupleBuilder<K, V> extends KafkaSpoutTupleBuilder<K, V> {
    public DefaultKafkaSpoutTupleBuilder(String... topics) {
        super(topics);
    }

    @Override
    public List<Object> buildTuple(ConsumerRecord<K, V> consumerRecord) {
        return new Values(consumerRecord.key(), consumerRecord.value());
    }
}