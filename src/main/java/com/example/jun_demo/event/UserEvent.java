package com.example.jun_demo.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 이벤트
 * 사용자 생성, 수정, 삭제 시 발생하는 이벤트
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {

    /**
     * 이벤트 타입
     */
    public enum EventType {
        CREATED, // 사용자 생성
        UPDATED, // 사용자 수정
        DELETED // 사용자 삭제
    }

    private EventType eventType;
    private Long userId;
    private String username;
    private String email;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    // 메타데이터
    private String ipAddress;
    private String userAgent;

    // 변경 전/후 데이터 (UPDATE인 경우)
    private String previousData;
    private String currentData;
}
