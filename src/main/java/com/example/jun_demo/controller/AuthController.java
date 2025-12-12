package com.example.jun_demo.controller;

import com.example.jun_demo.dto.UserDto;
import com.example.jun_demo.event.AuthEvent;
import com.example.jun_demo.service.KafkaProducerService;
import com.example.jun_demo.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDto.AuthResponse> signUp(
            @Valid @RequestBody UserDto.SignUpRequest request,
            HttpServletRequest httpRequest) {
        log.info("회원가입 요청: {}", request.getUsername());
        UserDto.AuthResponse response = userService.signUp(request);

        // Kafka 이벤트 발행 - 회원가입 성공 (로그인으로 간주)
        String sessionId = UUID.randomUUID().toString();
        AuthEvent event = AuthEvent.builder()
                .eventType(AuthEvent.EventType.LOGIN_SUCCESS)
                .userId(response.getUser().getId())
                .username(response.getUser().getUsername())
                .sessionId(sessionId)
                .timestamp(LocalDateTime.now())
                .ipAddress(getClientIP(httpRequest))
                .userAgent(httpRequest.getHeader("User-Agent"))
                .build();
        kafkaProducerService.sendAuthEvent(event, sessionId);

        return ResponseEntity.ok(response);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<UserDto.AuthResponse> login(
            @Valid @RequestBody UserDto.LoginRequest request,
            HttpServletRequest httpRequest) {
        log.info("로그인 요청: {}", request.getUsername());

        String sessionId = UUID.randomUUID().toString();
        try {
            UserDto.AuthResponse response = userService.login(request);

            // Kafka 이벤트 발행 - 로그인 성공
            AuthEvent event = AuthEvent.builder()
                    .eventType(AuthEvent.EventType.LOGIN_SUCCESS)
                    .userId(response.getUser().getId())
                    .username(response.getUser().getUsername())
                    .sessionId(sessionId)
                    .timestamp(LocalDateTime.now())
                    .ipAddress(getClientIP(httpRequest))
                    .userAgent(httpRequest.getHeader("User-Agent"))
                    .build();
            kafkaProducerService.sendAuthEvent(event, sessionId);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Kafka 이벤트 발행 - 로그인 실패
            AuthEvent event = AuthEvent.builder()
                    .eventType(AuthEvent.EventType.LOGIN_FAILURE)
                    .username(request.getUsername())
                    .sessionId(sessionId)
                    .timestamp(LocalDateTime.now())
                    .ipAddress(getClientIP(httpRequest))
                    .userAgent(httpRequest.getHeader("User-Agent"))
                    .failureReason(e.getMessage())
                    .build();
            kafkaProducerService.sendAuthEvent(event, sessionId);

            throw e;
        }
    }

    /**
     * 클라이언트 IP 주소 추출
     */
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
