package com.example.jun_demo.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 감사 이벤트
 * 모든 중요 시스템 작업에 대한 감사 추적
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {

    private String action; // 수행된 액션 (예: CREATE_USER, UPDATE_USER)
    private String entityType; // 엔티티 타입 (예: USER, AUTH)
    private String entityId; // 엔티티 ID

    private Long performedBy; // 액션 수행 사용자 ID
    private String performedByUsername; // 액션 수행 사용자명

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    // 변경 전/후 상태
    private String beforeState;
    private String afterState;

    // 메타데이터
    private String ipAddress;
    private String additionalInfo;

    // 결과
    private boolean success;
    private String errorMessage;
}
