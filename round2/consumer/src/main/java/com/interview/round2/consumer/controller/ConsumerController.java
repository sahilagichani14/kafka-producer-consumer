package com.interview.round2.consumer.controller;

import com.interview.round2.consumer.service.KafkaMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("emailStats")
public class ConsumerController {

    @Autowired
    private KafkaMessageListener kafkaMessageListener;

    @GetMapping("/uniqueEmailCount")
    public ResponseEntity<?> getUniqueEmailCount() {
        Set<String> uniqueEmails = kafkaMessageListener.getUniqueEmails();
        System.out.println(uniqueEmails);
        return ResponseEntity.ok(Map.of("uniqueEmailCount", kafkaMessageListener.getUniqueEmailCount()));
    }

    @GetMapping("/getUniqueEmailsByDomain/{domainToSearch}")
    public ResponseEntity<?> getUniqueDomainEmails(@PathVariable String domainToSearch) {
        Map<String, Set<String>> resultMap = kafkaMessageListener.getDomainEmailMap();

        resultMap.forEach((domain, emailSet) -> {
            System.out.println("Domain: " + domain);
            emailSet.forEach(email -> System.out.println(" - " + email));
        });

        // Optional: Filter only @gmail.com emails
        System.out.println("\nOnly " + domainToSearch + "  emails:");
        // Pattern domainPattern = Pattern.compile(".*@gmail\\.com");
        Set<String> res = resultMap.get(domainToSearch);

        if (res == null) {
            return ResponseEntity.ok("domain " + domainToSearch + " you are looking for doesn't exists");
        }
        Set<String> keySet = resultMap.keySet();
        return ResponseEntity.ok(Map.of("uniqueDomains", keySet.size()));
    }

}
