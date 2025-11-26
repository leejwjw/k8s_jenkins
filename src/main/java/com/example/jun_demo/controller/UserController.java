package com.example.jun_demo.controller;

import com.example.jun_demo.dto.UserDto;
import com.example.jun_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * 전체 사용자 조회
     */
    @GetMapping
    public ResponseEntity<List<UserDto.Response>> getAllUsers() {
        log.info("전체 사용자 조회 요청");
        List<UserDto.Response> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto.Response> getUserById(@PathVariable Long id) {
        log.info("사용자 조회 요청: {}", id);
        UserDto.Response user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        log.info("현재 사용자: {}", username);
        return ResponseEntity.ok("현재 로그인한 사용자: " + username);
    }

    /**
     * 사용자 정보 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto.Response> updateUser(
            @PathVariable Long id,
            @RequestBody UserDto.UpdateRequest request) {
        log.info("사용자 수정 요청: {}", id);
        UserDto.Response user = userService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }

    /**
     * 사용자 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("사용자 삭제 요청: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
