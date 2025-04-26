package com.interview.round2.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ConsumerApplication.class, args);
//		ConcurrentKafkaListenerContainerFactory bean = applicationContext.getBean(ConcurrentKafkaListenerContainerFactory.class);
//		System.out.println(bean);
	}

}
