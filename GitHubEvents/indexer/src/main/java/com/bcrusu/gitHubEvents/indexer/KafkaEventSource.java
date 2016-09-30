package com.bcrusu.gitHubEvents.indexer;

import com.bcrusu.gitHubEvents.common.kafka.CustomConsumerRebalanceListener;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import rx.Observable;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

class KafkaEventSource {
    private final String _topic;
    private final String _bootstrapServers;
    private final String _clientId;
    private final boolean _seekToBeginning;
    private Observable<Event> _observable = null;

    public KafkaEventSource(String clientId, String bootstrapServers, String topic, boolean seekToBeginning) {
        _seekToBeginning = seekToBeginning;
        if (clientId == null) throw new IllegalArgumentException("clientId");
        if (bootstrapServers == null) throw new IllegalArgumentException("bootstrapServers");
        if (topic == null) throw new IllegalArgumentException("topic");

        _clientId = clientId;
        _bootstrapServers = bootstrapServers;
        _topic = topic;
    }

    public Observable<Event> getObservable() {
        if (_observable == null) {
            _observable = createObservable();
        }

        return _observable;
    }

    private Observable<Event> createObservable() {
        Observable<Event> result = Observable.create(subscriber -> {
            try (KafkaConsumer<String, String> consumer = createKafkaConsumer()) {
                ConsumerRebalanceListener listener = new CustomConsumerRebalanceListener(consumer, _seekToBeginning);
                consumer.subscribe(Collections.singletonList(_topic), listener);

                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);

                for (ConsumerRecord<String, String> record : records) {
                    // stop reading records if the subscriber went away
                    if (subscriber.isUnsubscribed())
                        break;

                    Event event = new Event(record.key(), record.value());
                    subscriber.onNext(event);
                }
            }

            subscriber.onCompleted();
        });

        return result
                .retryWhen(errors -> errors.flatMap(error -> Observable.timer(5, TimeUnit.SECONDS)));
    }

    private KafkaConsumer<String, String> createKafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, _bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "gitHubEvents-indexer");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "gitHubEvents-indexer-" + _clientId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");

        return new KafkaConsumer<>(props);
    }
}
