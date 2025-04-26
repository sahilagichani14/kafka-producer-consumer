package com.interview.round2.consumer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Set;

@SpringBootTest
public class KafkaMessageListernerTest {

    @Autowired
    KafkaMessageListener kafkaMessageListener;

    @Test
    void processEmailTest() {
        kafkaMessageListener.processEmail("sahil@@gmail");
        Map<String, Set<String>> domainEmailMap = kafkaMessageListener.getDomainEmailMap();
        System.out.println(domainEmailMap);
    }

}
