package com.interview.round2.producer.controller;

import com.interview.round2.producer.service.ProducerService;
import entity.EmailEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/producer-app")
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    @PostMapping("/publish")
    public CompletableFuture<ResponseEntity<?>> sendEvents(@Valid @RequestBody List<EmailEvent> emailEvents) {
        try {
            List<CompletableFuture<?>> futures = new ArrayList<>();

            for (int i = 0; i < emailEvents.size(); i++) {
                futures.add(producerService.sendEmailEventsToTopic(emailEvents.get(i)));
            }

            // Wait until all futures complete
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(voidResult -> ResponseEntity.ok(Map.of("message", "All messages published")));
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            );
        }
    }

    @PostMapping("/publish/single")
    public CompletableFuture<?> sendSingleEvent(@Valid @RequestBody EmailEvent emailEvent) {
        CompletableFuture<?> completableFuture = producerService.sendEmailEventsToTopic(emailEvent)
                .thenApply(result -> ResponseEntity.ok(Map.of("message", "Message published")))
                .exceptionally(ex -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "Failed to publish message", "details", ex.getMessage()));
                });
        return completableFuture;
    }

}
