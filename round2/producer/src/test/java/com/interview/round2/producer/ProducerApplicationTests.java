package com.interview.round2.producer;

import entity.EmailEvent;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProducerApplicationTests {

	static Validator validator;

	@BeforeTestClass
	public static void setupValidatorInstance() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

	@Test
	void contextLoads() {
		EmailEvent emailEvent = new EmailEvent();
		emailEvent.setId(1L);
		emailEvent.setFrom("sahil@gmail.com");
		emailEvent.setTo(Collections.emptyList());
		emailEvent.setSubject("");
		emailEvent.setLocalDateTime(LocalDateTime.now());
		emailEvent.setContentBody("");

		Set<ConstraintViolation<EmailEvent>> violations = validator.validate(emailEvent);

		assertThat(violations.size()).isEqualTo(0);
	}

}
