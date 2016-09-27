package com.bcrusu.gitHubEvents.indexer;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import rx.Observable;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

class KafkaEventSource {
    private final String _topic;
    private final String _bootstrapServers;
    private final String _clientId;
    private Observable<Event> _observable = null;

    public KafkaEventSource(String clientId, String bootstrapServers, String topic) {
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
                consumer.subscribe(Collections.singletonList(_topic));
                ConsumerRecords<String, String> records = consumer.poll(1000);

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
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "gitHubEvents-indexer");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "gitHubEvents-indexer-" + _clientId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");

        return new KafkaConsumer<>(props);
    }
}
