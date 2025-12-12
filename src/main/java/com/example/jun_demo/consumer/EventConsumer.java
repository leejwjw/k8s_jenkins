package com.example.jun_demo.consumer;

import com.example.jun_demo.event.AuditEvent;
import com.example.jun_demo.event.AuthEvent;
import com.example.jun_demo.event.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka Consumer 서비스
 * 각 토픽의 이벤트를 수신하고 처리합니다.
 */
@Slf4j
@Component
public class EventConsumer {

    /**
     * 사용자 이벤트 리스너
     * Consumer Group: user-event-logger
     */
    @KafkaListener(topics = "user.events", groupId = "user-event-logger")
    public void consumeUserEvent(
            @Payload UserEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(value = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key) {

        log.info("=== User Event Received ===");
        log.info("Topic: {}, Partition: {}, Key: {}", topic, partition, key);
        log.info("Event Type: {}", event.getEventType());
        log.info("User ID: {}, Username: {}", event.getUserId(), event.getUsername());
        log.info("Timestamp: {}", event.getTimestamp());
        log.info("IP Address: {}", event.getIpAddress());

        // TODO: 여기서 실제 처리 로직 추가 (예: DB 저장, 알림 발송 등)
        // 예: userEventRepository.save(event);
    }

    /**
     * 인증 이벤트 리스너
     * Consumer Group: auth-event-logger
     */
    @KafkaListener(topics = "auth.events", groupId = "auth-event-logger")
    public void consumeAuthEvent(
            @Payload AuthEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(value = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key) {

        log.info("=== Auth Event Received ===");
        log.info("Topic: {}, Partition: {}, Key: {}", topic, partition, key);
        log.info("Event Type: {}", event.getEventType());
        log.info("Username: {}, User ID: {}", event.getUsername(), event.getUserId());
        log.info("Session ID: {}", event.getSessionId());
        log.info("Timestamp: {}", event.getTimestamp());

        if (event.getEventType() == AuthEvent.EventType.LOGIN_FAILURE) {
            log.warn("Login failure for user {}: {}", event.getUsername(), event.getFailureReason());
        }

        // TODO: 실제 처리 로직 추가
    }

    /**
     * 감사 로그 리스너
     * Consumer Group: audit-event-logger
     */
    @KafkaListener(topics = "audit.logs", groupId = "audit-event-logger")
    public void consumeAuditEvent(
            @Payload AuditEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(value = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key) {

        log.info("=== Audit Event Received ===");
        log.info("Topic: {}, Partition: {}, Key: {}", topic, partition, key);
        log.info("Action: {}, Entity: {} ({})", event.getAction(), event.getEntityType(), event.getEntityId());
        log.info("Performed by: {} (ID: {})", event.getPerformedByUsername(), event.getPerformedBy());
        log.info("Success: {}", event.isSuccess());
        log.info("Timestamp: {}", event.getTimestamp());

        if (!event.isSuccess()) {
            log.error("Audit event failed: {}", event.getErrorMessage());
        }

        // TODO: 감사 로그 영구 저장 로직 추가
        // 예: auditLogRepository.save(event);
    }
}
