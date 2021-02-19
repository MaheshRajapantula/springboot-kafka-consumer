package com.learning.kafka.springbootkafkaconsumer.config;

import com.learning.kafka.springbootkafkaconsumer.model.FinalKafkaTopic;
import com.learning.kafka.springbootkafkaconsumer.model.User;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfigurationProducer {

    @Bean
    public ProducerFactory producerFactoryRetry() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, User> kafkaTemplate() {
        return new KafkaTemplate<String, User>(producerFactoryRetry());
    }

    @Bean
    public KafkaTemplate<String, FinalKafkaTopic> kafkaFinalTemplate() {
        return new KafkaTemplate<String, FinalKafkaTopic>(producerFactoryRetry());
    }
}
