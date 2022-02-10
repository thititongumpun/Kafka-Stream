package io.confluent.developer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Consumer {
    public static void main(String[] args) throws InterruptedException {
        final Logger logger = LoggerFactory.getLogger(Consumer.class.getName());
        final String topic = "random-strings";
        final String bootstrapServer = "localhost:29092";
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getClasses());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-streams-101");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topic));

        for (;;) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(2000));
            for (ConsumerRecord<String, String> rec : records) {
                logger.info("Partitions: " + rec.partition());
                logger.info("Key: " + rec.key());
                logger.info("Value: " + rec.value());
                TimeUnit.SECONDS.sleep(2);
            }
        }
    }
}
