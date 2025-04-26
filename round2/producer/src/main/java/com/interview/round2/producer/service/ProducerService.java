package com.interview.round2.producer.service;

import com.interview.round2.producer.config.ProducerProperties;
import entity.EmailEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private Logger logger = LoggerFactory.getLogger(ProducerService.class);

    public CompletableFuture<?> sendEmailEventsToTopic(EmailEvent emailEvent) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(ProducerProperties.EMAIL_TOPIC_NAME, emailEvent);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Sent message=[{}] with offset=[{}]", emailEvent.toString(), result.getRecordMetadata().offset());
                } else {
                    logger.debug("Unable to send message=[{}] due to : {}", emailEvent.toString(), ex.getMessage());
                }
            });
            return future;
        } catch (Exception ex) {
            logger.error("ERROR : {}", ex.getMessage());
        }
        return new CompletableFuture<>();
    }
}
