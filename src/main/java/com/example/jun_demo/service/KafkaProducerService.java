package com.example.jun_demo.service;

import com.example.jun_demo.event.AuditEvent;
import com.example.jun_demo.event.AuthEvent;
import com.example.jun_demo.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Kafka Producer 서비스
 * 이벤트를 Kafka 토픽에 발행합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String USER_EVENTS_TOPIC = "user.events";
    private static final String AUTH_EVENTS_TOPIC = "auth.events";
    private static final String AUDIT_LOGS_TOPIC = "audit.logs";

    /**
     * 사용자 이벤트 발행
     * 
     * @param event 사용자 이벤트
     * @param key   파티션 키 (User ID 사용)
     */
    public void sendUserEvent(UserEvent event, String key) {
        log.info("Sending user event: type={}, userId={}, key={}",
                event.getEventType(), event.getUserId(), key);

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(USER_EVENTS_TOPIC, key, event);

        addCallback(future, "User Event");
    }

    /**
     * 인증 이벤트 발행
     * 
     * @param event 인증 이벤트
     * @param key   파티션 키 (Session ID 또는 User ID 사용)
     */
    public void sendAuthEvent(AuthEvent event, String key) {
        log.info("Sending auth event: type={}, username={}, key={}",
                event.getEventType(), event.getUsername(), key);

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(AUTH_EVENTS_TOPIC, key, event);

        addCallback(future, "Auth Event");
    }

    /**
     * 감사 이벤트 발행
     * 
     * @param event 감사 이벤트
     * @param key   파티션 키 (Entity ID 사용)
     */
    public void sendAuditEvent(AuditEvent event, String key) {
        log.info("Sending audit event: action={}, entityType={}, entityId={}",
                event.getAction(), event.getEntityType(), event.getEntityId());

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(AUDIT_LOGS_TOPIC, key, event);

        addCallback(future, "Audit Event");
    }

    /**
     * 이벤트 발행 결과 콜백 처리
     */
    private void addCallback(ListenableFuture<SendResult<String, Object>> future, String eventType) {
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("{} sent successfully to partition {}",
                        eventType, result.getRecordMetadata().partition());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Failed to send {}: {}", eventType, ex.getMessage(), ex);
            }
        });
    }
}
