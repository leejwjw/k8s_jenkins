package com.example.jun_demo.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 인증 이벤트
 * 로그인, 로그아웃 등 인증 관련 이벤트
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthEvent {

    /**
     * 인증 이벤트 타입
     */
    public enum EventType {
        LOGIN_SUCCESS, // 로그인 성공
        LOGIN_FAILURE, // 로그인 실패
        LOGOUT, // 로그아웃
        TOKEN_REFRESH // 토큰 갱신
    }

    private EventType eventType;
    private Long userId;
    private String username;
    private String sessionId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    // 메타데이터
    private String ipAddress;
    private String userAgent;
    private String deviceInfo;

    // 실패 정보 (로그인 실패 시)
    private String failureReason;
}
