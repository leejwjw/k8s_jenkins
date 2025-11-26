package com.example.jun_demo.service;

import com.example.jun_demo.dto.UserDto;
import com.example.jun_demo.entity.User;
import com.example.jun_demo.mapper.UserMapper;
import com.example.jun_demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     */
    public UserDto.AuthResponse signUp(UserDto.SignUpRequest request) {
        // 중복 체크
        if (userMapper.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 존재하는 사용자명입니다");
        }
        if (userMapper.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다");
        }

        // 사용자 생성
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role("USER")
                .active(true)
                .build();

        userMapper.insert(user);

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getUsername());

        return UserDto.AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .user(convertToDto(user))
                .build();
    }

    /**
     * 로그인
     */
    public UserDto.AuthResponse login(UserDto.LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        if (!user.getActive()) {
            throw new RuntimeException("비활성화된 계정입니다");
        }

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getUsername());

        return UserDto.AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .user(convertToDto(user))
                .build();
    }

    /**
     * 사용자 목록 조회
     */
    @Transactional(readOnly = true)
    public List<UserDto.Response> getAllUsers() {
        return userMapper.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 단건 조회
     */
    @Transactional(readOnly = true)
    public UserDto.Response getUserById(Long id) {
        User user = userMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        return convertToDto(user);
    }

    /**
     * 사용자 정보 수정
     */
    public UserDto.Response updateUser(Long id, UserDto.UpdateRequest request) {
        User user = userMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userMapper.update(user);

        return convertToDto(user);
    }

    /**
     * 사용자 삭제
     */
    public void deleteUser(Long id) {
        if (!userMapper.findById(id).isPresent()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다");
        }
        userMapper.deleteById(id);
    }

    /**
     * Entity를 DTO로 변환
     */
    private UserDto.Response convertToDto(User user) {
        return UserDto.Response.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
