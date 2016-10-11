package com.bcrusu.gitHubEvents.streaming.storm;

import com.bcrusu.gitHubEvents.common.cli.KafkaProperties;
import com.bcrusu.gitHubEvents.common.gitHub.GitHubEvent;
import com.bcrusu.gitHubEvents.common.kafka.GitHubEventSerde;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.storm.kafka.spout.*;
import org.apache.storm.tuple.Fields;

import java.util.HashMap;
import java.util.Map;

public class KafkaSpoutFactory {
    public static final String STREAM_NAME = "gitHubEvents";

    private final KafkaProperties _kafkaProperties;

    public KafkaSpoutFactory(KafkaProperties kafkaProperties) {
        _kafkaProperties = kafkaProperties;
    }

    public KafkaSpout<String, GitHubEvent> createKafkaSpout() {
        Map<String, Object> kafkaConsumerProps = getKafkaConsumerProperties();
        KafkaSpoutStreams kafkaSpoutStreams = getKafkaSpoutStreams();
        KafkaSpoutTuplesBuilder<String, GitHubEvent> tuplesBuilder = getTuplesBuilder();

        KafkaSpoutConfig<String, GitHubEvent> kafkaSpoutConfig = new KafkaSpoutConfig.Builder<>(kafkaConsumerProps, kafkaSpoutStreams, tuplesBuilder)
                .setOffsetCommitPeriodMs(10_000)
                .setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.EARLIEST)
                .setMaxUncommittedOffsets(250)
                .build();

        return new KafkaSpout<>(kafkaSpoutConfig);
    }

    private Map<String, Object> getKafkaConsumerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, _kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "gitHubEvents-streaming-storm");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GitHubEventSerde.class.getName());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");

        return props;
    }

    private KafkaSpoutStreams getKafkaSpoutStreams() {
        Fields outputFields = new Fields("key", "value");
        return new KafkaSpoutStreamsNamedTopics.Builder(outputFields, STREAM_NAME, new String[]{_kafkaProperties.getTopic()})
                .build();
    }

    private KafkaSpoutTuplesBuilder<String, GitHubEvent> getTuplesBuilder() {
        return new KafkaSpoutTuplesBuilderNamedTopics.Builder<>(
                new DefaultKafkaSpoutTupleBuilder<String, GitHubEvent>(_kafkaProperties.getTopic()))
                .build();
    }
}
