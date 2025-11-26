package com.example.jun_demo.config;

import com.example.jun_demo.entity.User;
import com.example.jun_demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 이미 데이터가 있으면 초기화하지 않음
        if (userMapper.findAll().size() > 0) {
            log.info("데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        log.info("초기 데이터 삽입 시작...");

        // 비밀번호: password123
        String encodedPassword = passwordEncoder.encode("password123");

        // 관리자 계정
        User admin = User.builder()
                .username("admin")
                .email("admin@example.com")
                .password(encodedPassword)
                .name("관리자")
                .role("ADMIN")
                .active(true)
                .build();
        userMapper.insert(admin);
        log.info("관리자 계정 생성: {}", admin.getUsername());

        // 테스트 사용자
        User testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password(encodedPassword)
                .name("테스트 사용자")
                .role("USER")
                .active(true)
                .build();
        userMapper.insert(testUser);
        log.info("테스트 사용자 생성: {}", testUser.getUsername());

        // John Doe
        User john = User.builder()
                .username("john")
                .email("john@example.com")
                .password(encodedPassword)
                .name("John Doe")
                .role("USER")
                .active(true)
                .build();
        userMapper.insert(john);
        log.info("사용자 생성: {}", john.getUsername());

        // Jane Smith
        User jane = User.builder()
                .username("jane")
                .email("jane@example.com")
                .password(encodedPassword)
                .name("Jane Smith")
                .role("USER")
                .active(true)
                .build();
        userMapper.insert(jane);
        log.info("사용자 생성: {}", jane.getUsername());

        log.info("초기 데이터 삽입 완료! 총 {} 명의 사용자가 생성되었습니다.", userMapper.findAll().size());
        log.info("로그인 정보 - 사용자명: admin, testuser, john, jane / 비밀번호: password123");
    }
}
