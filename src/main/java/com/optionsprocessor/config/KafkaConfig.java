package com.optionsprocessor.config;

import com.optionsprocessor.model.OptionEvent;
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
public class KafkaConfig {
    @Bean
    public ProducerFactory<String, OptionEvent> producerFactory() {
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.0.0.237:9092,10.0.0.237:9093,10.0.0.237:9094");
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, 2);
        configProperties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 50000000);
        return new DefaultKafkaProducerFactory<String, OptionEvent>(configProperties);
    }

    @Bean
    public KafkaTemplate<String, OptionEvent> kafkaTemplate() {
        return new KafkaTemplate<String, OptionEvent>(producerFactory());
    }
}
