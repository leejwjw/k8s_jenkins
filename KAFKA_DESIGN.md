# Kafka 설계 문서

## 개요

이 문서는 jun-demo 프로젝트에 통합된 Apache Kafka 메시징 시스템의 설계 결정 사항과 운영 지침을 설명합니다.

## 아키텍처 결정사항

### 1. 파티션 수 (Partition Count)

**결정**: 토픽당 3개 파티션 (감사 로그는 5개)

**근거**:
- **TPS 고려**: 초당 1,000~3,000 건의 이벤트 처리 가능 (파티션당 ~500 TPS)
- **확장성**: 컨슈머를 3개까지 확장 가능 (파티션 수 = 최대 컨슈머 수)
- **부하 분산**: User ID를 키로 사용하여 동일 사용자의 이벤트는 동일 파티션으로 라우팅
- **감사 로그**: 더 많은 이벤트가 예상되어 5개 파티션 사용

**조정 기준**:
```
필요 파티션 수 = (목표 TPS) / (파티션당 처리 가능 TPS)
```

### 2. 키 전략 (Key Strategy)

**사용자 이벤트 (`user.events`)**:
- 키: `String.valueOf(userId)`
- 목적: 동일 사용자의 모든 이벤트를 순서대로 처리
- 장점: 사용자별 이벤트 순서 보장 (생성 → 수정 → 삭제)

**인증 이벤트 (`auth.events`)**:
- 키: `sessionId` (UUID)
- 목적: 세션별 이벤트 추적
- 장점: 동일 세션의 이벤트를 함께 처리

**감사 로그 (`audit.logs`)**:
- 키: `entityId`
- 목적: 엔티티별 감사 추적
- 장점: 특정 엔티티에 대한 모든 변경 이력 순서 보장

**트레이드오프**:
- **순서 보장** (현재 방식): 동일 키는 동일 파티션 → 순서 보장, Key Skew 가능
- **부하 분산**: null 키 또는 랜덤 키 → 균등 분산, 순서 보장 불가

### 3. 컨슈머 그룹 (Consumer Groups)

**현재 구성**:
- `user-event-logger`: 사용자 이벤트 로깅
- `auth-event-logger`: 인증 이벤트 로깅
- `audit-event-logger`: 감사 로그 저장

**크기 조정 가이드**:
- 컨슈머 수 ≤ 파티션 수 (초과 시 유휴 컨슈머 발생)
- 현재: 각 그룹당 1개 컨슈머 (3개 파티션 활용 가능)
- 부하 증가 시: 컨슈머를 최대 3개까지 추가 (파티션 수만큼)

**확장 예시**:
```yaml
# 단일 애플리케이션 인스턴스
user-event-logger: 1개 컨슈머 (3개 파티션 모두 처리)

# 3개 인스턴스 (Kubernetes 환경)
user-event-logger-instance-1: 파티션 0 처리
user-event-logger-instance-2: 파티션 1 처리
user-event-logger-instance-3: 파티션 2 처리
```

### 4. 복제 계수 (Replication Factor)

**개발 환경**: 1
- 단일 브로커에서 실행
- 데이터 손실 시 재생성 가능

**운영 환경 권장**: 2-3
- **2 (최소 권장)**: 1개 브로커 장애 허용
- **3 (고가용성)**: 2개 브로커 장애 허용

**성능 영향**:
- 복제 계수 증가 → 디스크 사용량 증가 (복제 계수 × 데이터 크기)
- 복제 계수 증가 → 네트워크 대역폭 사용 증가
- 복제 계수 증가 → Ack 대기 시간 증가

**권장 설정** (운영 환경):
```properties
kafka.topic.replication-factor=3
# Producer 설정
acks=all  # 모든 복제본 확인 (최대 안정성)
min.insync.replicas=2  # 최소 2개 복제본 동기화 필요
```

### 5. 보존 정책 (Retention Policy)

**현재 설정**: 7일 (168시간)

**근거**:
- 대부분의 이벤트는 실시간 처리
- 7일간의 히스토리는 문제 추적 및 재처리에 충분
- 디스크 용량 관리 (오래된 데이터 자동 삭제)

**디스크 용량 계산**:
```
필요 용량 = (초당 이벤트 수) × (이벤트 크기) × (보존 시간) × (복제 계수)
예시: 100 TPS × 1KB × 7일 × 1 = ~60GB
```

**조정 시나리오**:
- **규정 준수**: 장기 보관 필요 시 30일 이상
- **디스크 부족**: 3일로 단축
- **재처리 빈도**: 자주 재처리한다면 더 길게

### 6. 성능 최적화 설정

**Producer 설정**:
```properties
acks=1                    # 리더 확인만 (빠른 응답)
retries=3                 # 실패 시 3회 재시도
batch.size=16384          # 16KB 배치
linger.ms=10              # 10ms 대기 후 전송
compression.type=snappy   # Snappy 압축 (CPU↓, 네트워크↓)
```

**Consumer 설정**:
```properties
auto.offset.reset=earliest       # 처음부터 읽기
enable.auto.commit=true          # 자동 오프셋 커밋
auto.commit.interval.ms=1000     # 1초마다 커밋
```

## 토픽 구성

### user.events
- **목적**: 사용자 생명주기 이벤트
- **파티션**: 3개
- **키**: User ID
- **보존**: 7일
- **이벤트 타입**: CREATED, UPDATED, DELETED

### auth.events
- **목적**: 인증 및 세션 이벤트
- **파티션**: 3개
- **키**: Session ID
- **보존**: 7일
- **이벤트 타입**: LOGIN_SUCCESS, LOGIN_FAILURE, LOGOUT

### audit.logs
- **목적**: 전체 시스템 감사 추적
- **파티션**: 5개 (높은 처리량)
- **키**: Entity ID
- **보존**: 7일 (규정에 따라 연장 가능)

## 모니터링 메트릭

### 주요 지표
1. **파티션별 메시지 수**: Key Skew 감지
2. **컨슈머 Lag**: 처리 지연 확인
3. **프로듀서 에러율**: 발행 실패 모니터링
4. **디스크 사용량**: 보존 정책 적절성 확인

### Kafka 명령어
```bash
# 토픽 상태 확인
kafka-topics.sh --describe --topic user.events --bootstrap-server localhost:9092

# 컨슈머 그룹 Lag 확인
kafka-consumer-groups.sh --describe --group user-event-logger --bootstrap-server localhost:9092

# 파티션별 메시지 수
kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic user.events
```

## 트러블슈팅

### Key Skew 발생
**증상**: 특정 파티션에 메시지 집중
**원인**: 특정 사용자의 활동이 매우 많음
**해결책**:
1. 파티션 수 증가 (더 세밀한 분산)
2. 복합 키 사용 (예: `userId + timestamp`)
3. 핫 키 감지 및 별도 처리

### 컨슈머 Lag 증가
**증상**: 메시지 처리 지연
**원인**: 컨슈머 처리 속도 < 메시지 생성 속도
**해결책**:
1. 컨슈머 인스턴스 증가
2. 배치 처리 크기 조정
3. 비동기 처리 추가

### 디스크 용량 부족
**증상**: Kafka 브로커 디스크 full
**원인**: 보존 시간이 너무 길거나 메시지 급증
**해결책**:
1. 보존 시간 단축
2. 로그 압축 활성화
3. 디스크 증설

## 운영 환경 배포 체크리스트

- [] Kafka 클러스터 최소 3개 브로커 구성
- [] `replication-factor=3` 설정
- [] `min.insync.replicas=2` 설정
- [] ZooKeeper quorum 구성 (3개 이상)
- [] 모니터링 도구 설정 (Prometheus + Grafana)
- [] 디스크 용량 알람 설정
- [] 컨슈머 Lag 알람 설정
- [] 백업 및 복구 절차 수립
