# l0gin - React + Spring Boot + JWT + MyBatis


풀스택 웹 구성 
Jenkins K8s 세팅 테스트

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
│   ├── config/          # 설정 클래스 (Security, JWT, Kafka)
│   ├── controller/      # REST API 컨트롤러
│   ├── dto/             # 데이터 전송 객체
│   ├── entity/          # JPA 엔티티
│   ├── event/           # Kafka 이벤트 모델
│   ├── exception/       # 예외 처리
│   ├── mapper/          # MyBatis 매퍼
│   ├── security/        # JWT 필터
│   ├── service/         # 비즈니스 로직
│   ├── consumer/        # Kafka 컨슈머
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

## 📨 Kafka 메시징

### Kafka 개요

이 프로젝트는 이벤트 기반 아키텍처를 위해 Apache Kafka를 통합했습니다:
- **사용자 이벤트**: 생성, 수정, 삭제
- **인증 이벤트**: 로그인 성공/실패, 로그아웃
- **감사 로그**: 모든 중요 작업 추적

### Kafka 설정

#### 토픽 구성

| 토픽 | 파티션 수 | 복제 계수 | 보존 기간 | 용도 |
|------|----------|----------|----------|------|
| `user.events` | 3 | 1 (개발) | 7일 | 사용자 생명주기 이벤트 |
| `auth.events` | 3 | 1 (개발) | 7일 | 인증 이벤트 |
| `audit.logs` | 5 | 1 (개발) | 7일 | 감사 추적 |

#### 파티션 전략

- **Key**: User ID, Session ID, Entity ID 사용
- **목적**: 동일 엔티티의 이벤트 순서 보장
- **컨슈머 그룹**: `user-event-logger`, `auth-event-logger`, `audit-event-logger`

### Docker로 Kafka 실행

```bash
# Kafka 시작 (단일 브로커)
docker run -d --name kafka -p 9092:9092 apache/kafka:latest

# 토픽 확인
docker exec kafka kafka-topics.sh --list --bootstrap-server localhost:9092

# 이벤트 모니터링
docker exec kafka kafka-console-consumer.sh --topic user.events --bootstrap-server localhost:9092 --from-beginning
```

### 이벤트 예시

#### 사용자 생성 이벤트
```json
{
  "eventType": "CREATED",
  "userId": 1,
  "username": "testuser",
  "email": "test@example.com",
  "timestamp": "2025-12-12T14:30:00",
  "ipAddress": "127.0.0.1"
}
```

#### 로그인 성공 이벤트
```json
{
  "eventType": "LOGIN_SUCCESS",
  "userId": 1,
  "username": "testuser",
  "sessionId": "uuid-here",
  "timestamp": "2025-12-12T14:30:00",
  "ipAddress": "127.0.0.1",
  "userAgent": "Mozilla/5.0..."
}
```

### 설계 문서

상세한 Kafka 설계 및 운영 가이드는 [KAFKA_DESIGN.md](KAFKA_DESIGN.md)를 참조하세요:
- 파티션 수 결정 근거
- 키 전략 (순서 보장 vs 부하 분산)
- 컨슈머 그룹 크기 조정
- 복제 계수 트레이드오프
- 모니터링 및 트러블슈팅

