package com.interview.round2.consumer.service;

import com.interview.round2.consumer.config.ConsumerProperties;
import entity.EmailEvent;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class KafkaMessageListener {

    Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    @Getter
    Set<String> uniqueEmails = ConcurrentHashMap.newKeySet();

    Object lockObj = new Object();

    @Getter
    Map<String, Set<String>> domainEmailMap = new ConcurrentHashMap<>();

    // Regex to match domain from email (e.g., @gmail.com)
    private static final Pattern DOMAIN_PATTERN = Pattern.compile("@[a-zA-Z0-9.-]+");

    public void processEmail(String email) {
        uniqueEmails.add(email); // add to a set for uniqueEmails

        Matcher matcher = DOMAIN_PATTERN.matcher(email);
        if (matcher.find()) {
            String domain = matcher.group();
            domainEmailMap.computeIfAbsent(domain, k -> new HashSet<>()).add(email);
        }
    }

    public int getUniqueEmailCount() {
        return uniqueEmails.size();
    }

    @KafkaListener(
            groupId = ConsumerProperties.GROUP_ID,
            topicPartitions = {
                    @TopicPartition(
                            topic = ConsumerProperties.EMAIL_TOPIC_NAME,
                            partitionOffsets = @PartitionOffset(initialOffset = "0", partition = "0")
                    )
            }
    )
    // @KafkaListener(topics = ConsumerProperties.EMAIL_TOPIC_NAME, groupId = ConsumerProperties.GROUP_ID)
    public void consumeEvents(EmailEvent emailEvent) {
        synchronized (lockObj) {
            processEmail(emailEvent.getFrom());
            for (String email : emailEvent.getTo()) {
                processEmail(email);
            }
            log.info("consumer 1 consume the events {} ", emailEvent);
        }
    }

    /*
    // @KafkaListener(topics = ConsumerProperties.EMAIL_TOPIC_NAME, groupId = ConsumerProperties.GROUP_ID)
    @KafkaListener(
            groupId = ConsumerProperties.GROUP_ID,
            topicPartitions = {
                    @TopicPartition(
                            topic = ConsumerProperties.EMAIL_TOPIC_NAME,
                            partitionOffsets = @PartitionOffset(initialOffset = "2", partition = "1")
                    )
            }
    )
    public void consumer2(EmailEvent emailEvent) {
        uniqueEmails.add(emailEvent.getFrom());
        uniqueEmails.addAll(emailEvent.getTo());
        log.info("consumer 2 consume the events {} ", emailEvent);
    }
     */

    /*
    @KafkaListener(groupId = ConsumerProperties.GROUP_ID_2, topicPartitions = {
            @TopicPartition(topic = ConsumerProperties.EMAIL_TOPIC_NAME, partitions = {"0-1, 2"}, partitionOffsets = {
                    @PartitionOffset(partition = "*", initialOffset = "0")
            })
    })
    // Not scalable (this disables Kafka's automatic partition assignment and balancing)
    public void consumer3(ConsumerRecord<String, EmailEvent> consumerRecord) {
        int partition = consumerRecord.partition();
        if (partition == 0) {
            log.info("key: {}, headers: {}", consumerRecord.key(), consumerRecord.headers());
            log.info("consumer 3 handling with partition {} with some different process..", partition);
        }
        EmailEvent emailEvent = consumerRecord.value();
        uniqueEmails.add(emailEvent.getFrom());
        uniqueEmails.addAll(emailEvent.getTo());
        log.info("consumer 3 consume the events {} ", emailEvent);
    }
     */

    /*
    // Run both consumers in separate Spring Boot apps or threads/containers, this is just for testing so I can use this.
    @KafkaListener(topics = ConsumerProperties.EMAIL_TOPIC_NAME, groupId = ConsumerProperties.GROUP_ID_3, containerFactory = "kafkaListenerContainerFactory1")
    public void consumer4(@Payload EmailEvent emailEvent) {
        uniqueEmails.add(emailEvent.getFrom());
        uniqueEmails.addAll(emailEvent.getTo());
        log.info("consumer 4 consume the events {}", emailEvent);
    }

    @KafkaListener(topics = ConsumerProperties.EMAIL_TOPIC_NAME, groupId = ConsumerProperties.GROUP_ID_3, containerFactory = "kafkaListenerContainerFactory2")
    public void consumer5(@Payload EmailEvent emailEvent) {
        uniqueEmails.add(emailEvent.getFrom());
        uniqueEmails.addAll(emailEvent.getTo());
        log.info("consumer 5 consume the events {}", emailEvent);
    }

    @KafkaListener(topics = ConsumerProperties.EMAIL_TOPIC_NAME, groupId = ConsumerProperties.GROUP_ID_3, concurrency = "3")
    public void consumer6(@Payload EmailEvent emailEvent, Acknowledgment acknowledgment) {
        uniqueEmails.add(emailEvent.getFrom());
        uniqueEmails.addAll(emailEvent.getTo());
        log.info("consumer 6 consume the events {}", emailEvent);
        acknowledgment.acknowledge(); // manually aknowledge props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    }
     */

}
