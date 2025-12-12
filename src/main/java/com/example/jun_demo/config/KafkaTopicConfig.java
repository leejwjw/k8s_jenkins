package com.example.jun_demo.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka 토픽 설정 클래스
 * 애플리케이션에서 사용할 토픽을 정의합니다.
 */
@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${kafka.topic.partitions:3}")
    private int partitions;

    @Value("${kafka.topic.replication-factor:1}")
    private short replicationFactor;

    @Value("${kafka.topic.retention-hours:168}")
    private String retentionHours;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    /**
     * 사용자 이벤트 토픽
     * - 사용자 생성, 수정, 삭제 이벤트
     * - 파티션 수: 3 (User ID로 파티셔닝)
     */
    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name("user.events")
                .partitions(partitions)
                .replicas(replicationFactor)
                .config("retention.ms", String.valueOf(Long.parseLong(retentionHours) * 3600000))
                .build();
    }

    /**
     * 인증 이벤트 토픽
     * - 로그인, 로그아웃 이벤트
     * - 파티션 수: 3
     */
    @Bean
    public NewTopic authEventsTopic() {
        return TopicBuilder.name("auth.events")
                .partitions(partitions)
                .replicas(replicationFactor)
                .config("retention.ms", String.valueOf(Long.parseLong(retentionHours) * 3600000))
                .build();
    }

    /**
     * 감사 로그 토픽
     * - 모든 중요 작업에 대한 감사 추적
     * - 파티션 수: 5 (높은 처리량)
     */
    @Bean
    public NewTopic auditLogsTopic() {
        return TopicBuilder.name("audit.logs")
                .partitions(5) // 감사 로그는 더 많은 파티션 사용
                .replicas(replicationFactor)
                .config("retention.ms", String.valueOf(Long.parseLong(retentionHours) * 3600000))
                .build();
    }
}
