package com.interview.round2.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfigProducer {

    @Bean
    public NewTopic emailTopic() {
        // return new NewTopic(ProducerProperties.EMAIL_TOPIC_NAME, 3, (short) 1);
        return TopicBuilder
                .name(ProducerProperties.EMAIL_TOPIC_NAME)
//                .partitions(2)
                .build();
    }

    @Bean
    public Map<String,Object> producerConfig(){
        Map<String,Object> props=new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        // props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // if we don't want producer to have duplicates
        // props.put(ProducerConfig.ACKS_CONFIG, "all"); // producer waits for acks from all replicas, this ensures data is written to all replicas & is durable in case of broker failure
        // props.put(ProducerConfig.RETRIES_CONFIG, 3); // no of retry
        return props;
    }

    @Bean
    public ProducerFactory<String,Object> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String,Object> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

}
