package com.agroventa.authentication.infrastructure.notification;

import com.agroventa.authentication.domain.port.NotificationPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.Map;

/**
 * Sends notification events to SQS. A Lambda consumer reads
 * from the queue and sends the actual emails via AWS SES.
 */
@Component
public class SqsNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(SqsNotificationAdapter.class);

    private final SqsClient   sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${app.aws.sqs.notifications-queue-url:}")
    private String notificationsQueueUrl;

    public SqsNotificationAdapter(SqsClient sqsClient, ObjectMapper objectMapper) {
        this.sqsClient    = sqsClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> sendWelcomeEmail(String toEmail, String firstName) {
        Map<String, Object> payload = Map.of(
                "type",      "WELCOME",
                "toEmail",   toEmail,
                "firstName", firstName
        );
        return publishToSqs(payload);
    }

    @Override
    public Mono<Void> sendPasswordResetCode(String toEmail, String firstName, String code) {
        Map<String, Object> payload = Map.of(
                "type",      "PASSWORD_RESET",
                "toEmail",   toEmail,
                "firstName", firstName,
                "code",      code
        );
        return publishToSqs(payload);
    }

    private Mono<Void> publishToSqs(Map<String, Object> payload) {
        if (notificationsQueueUrl == null || notificationsQueueUrl.isBlank()) {
            log.warn("SQS notifications queue URL not configured — skipping notification");
            return Mono.empty();
        }
        return Mono.fromCallable(() -> {
                    String body = objectMapper.writeValueAsString(payload);
                    sqsClient.sendMessage(SendMessageRequest.builder()
                            .queueUrl(notificationsQueueUrl)
                            .messageBody(body)
                            .build());
                    return body;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(b -> log.debug("Notification sent to SQS: {}", b))
                .doOnError(e -> log.error("Failed to send notification to SQS", e))
                .then();
    }
}
