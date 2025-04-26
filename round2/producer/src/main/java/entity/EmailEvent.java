package entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmailEvent {

    private Long id;

    @Email
    private String from;

    @NotEmpty
    @Size(min = 1, message = "Should send email to at least 1 person")
    @Valid
    private List<@Email(message = "Each recipient email must be valid") String> to;

    private String subject;

    private LocalDateTime localDateTime;

    private String contentBody;

}

//{
//        "id": 1,
//        "from": "sender@example.com",
//        "to": [
//        "recipient1@example.com",
//        "recipient2@example.com"
//        ],
//        "subject": "Welcome Email",
//        "localDateTime": "2025-04-15T14:30:00",
//        "contentBody": "Hello! This is a test email body content."
//}