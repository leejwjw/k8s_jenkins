# l0gin - React + Spring Boot + JWT + MyBatis

## 🎯 프로젝트 개요

이 프로젝트는 다음 기술 스택으로 구성된 풀스택 웹 애플리케이션입니다:

### Backend
- **Spring Boot 3.5.8**
- **Spring Security + JWT** (인증/인가)
- **MyBatis** (데이터베이스 매핑)
- **MySQL** / H2 Database
- **Java 17**

### Frontend
- **React 18**
- **Vite** (빌드 도구)
- **Modern CSS** (글래스모피즘, 그라디언트)

## 📋 기능

###  백엔드 API
- ✨ JWT 토큰 기반 인증
- 🔐 회원가입 (POST `/api/auth/signup`)
- 🔑 로그인 (POST `/api/auth/login`)
- 👥 사용자 목록 조회 (GET `/api/users`)
- 👤 사용자 단건 조회 (GET `/api/users/{id}`)
- ✏️ 사용자 수정 (PUT `/api/users/{id}`)
- 🗑️ 사용자 삭제 (DELETE `/api/users/{id}`)
- 📊 현재 사용자 정보 (GET `/api/users/me`)

###  프론트엔드

- 🔐 로그인/회원가입 페이지
- 📊 대시보드 (사용자 관리)
- 🔄 실시간 API 연동
- 💾 JWT 토큰 관리 (localStorage)

##  실행 방법

### 사전 요구사항

1. **Java 17 이상** 설치
2. **Node.js 16 이상** 설치
3. **MySQL** 설치 (선택사항 - H2 사용 가능)

### 1. 데이터베이스 설정

#### MySQL (localhost) - 현재 설정

1. **MySQL 설치**

2. **MySQL 서비스 시작**

3. **데이터베이스 자동 생성**

4. **연결 정보** (`application.properties`)

** 중요**: MySQL root 계정에 비밀번호가 있다면 `application.properties`의 `password=` 에 입력하세요!

** 상세 가이드**: `MYSQL_SETUP_GUIDE.md` 참조

### 2. 백엔드 실행

```bash
# Gradle 빌드 (Windows)
.\gradlew clean build

# 또는 직접 실행
.\gradlew bootRun
```

백엔드 서버가 **http://localhost:8080** 에서 실행됩니다.

### 3. 프론트엔드 실행

```bash
cd frontend
npm install
npm run dev
```

프론트엔드가 **http://localhost:3000** 에서 실행됩니다.


### 테스트 계정

MySQL을 사용하는 경우 다음 계정으로 로그인할 수 있습니다:

- **관리자**
  - Username: `admin`
  - Password: `password123`

- **일반 사용자**
  - Username: `testuser`
  - Password: `password123`

### API 테스트

#### 회원가입
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "password123",
    "name": "새 사용자"
  }'
```

#### 로그인
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "password123"
  }'
```

#### 사용자 목록 조회 (JWT 토큰 필요)
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📁 프로젝트 구조

```
jun-demo/
├── src/main/java/com/example/jun_demo/
│   ├── config/          # 설정 클래스 (Security, JWT)
│   ├── controller/      # REST API 컨트롤러
│   ├── dto/             # 데이터 전송 객체
│   ├── entity/          # JPA 엔티티
│   ├── exception/       # 예외 처리
│   ├── mapper/          # MyBatis 매퍼
│   ├── security/        # JWT 필터
│   ├── service/         # 비즈니스 로직
│   └── util/            # 유틸리티 (JWT)
├── src/main/resources/
│   ├── application.properties  # 애플리케이션 설정
│   └── db/init.sql            # MySQL 초기화 스크립트
└── frontend/
    ├── src/
    │   ├── components/   # React 컴포넌트
    │   │   ├── AuthPage.jsx      # 로그인/회원가입
    │   │   ├── Dashboard.jsx     # 대시보드
    │   │   ├── Header.jsx        # 헤더
    │   │   ├── Hero.jsx          # 히어로 섹션
    │   │   ├── Features.jsx      # 기능 카드
    │   │   └── Stats.jsx         # 통계
    │   ├── App.jsx       # 메인 앱
    │   └── index.css     # 글로벌 스타일
    └── package.json      # 의존성

