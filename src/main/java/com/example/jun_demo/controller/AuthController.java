package com.example.jun_demo.controller;

import com.example.jun_demo.dto.UserDto;
import com.example.jun_demo.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDto.AuthResponse> signUp(@Valid @RequestBody UserDto.SignUpRequest request) {
        log.info("회원가입 요청: {}", request.getUsername());
        UserDto.AuthResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<UserDto.AuthResponse> login(@Valid @RequestBody UserDto.LoginRequest request) {
        log.info("로그인 요청: {}", request.getUsername());
        UserDto.AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
