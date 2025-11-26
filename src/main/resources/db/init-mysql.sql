-- ============================================
-- l0gin - MySQL 초기화 스크립트
-- ============================================

-- 데이터베이스 생성 (application.properties에서 자동 생성되므로 선택사항)
CREATE DATABASE IF NOT EXISTS jun_demo 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE jun_demo;

-- 기존 테이블이 있으면 삭제
DROP TABLE IF EXISTS users;

-- 사용자 테이블 생성
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) DEFAULT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 초기 테스트 데이터 삽입
-- 비밀번호: password123 (BCrypt 해시)
INSERT INTO users (username, email, password, name, role) VALUES
('admin', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '관리자', 'ADMIN'),
('testuser', 'test@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트 사용자', 'USER'),
('john', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John Doe', 'USER'),
('jane', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane Smith', 'USER');

-- 데이터 확인
SELECT * FROM users;

-- 사용자 수 확인
SELECT COUNT(*) as total_users FROM users;

-- 권한별 사용자 수
SELECT role, COUNT(*) as count FROM users GROUP BY role;
