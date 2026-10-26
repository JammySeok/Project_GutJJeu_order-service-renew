# GutJJeu: order-renew

세상에 없던 나만의 굿즈를 만들고 판매하는 이커머스 플랫폼 GutJJeu의 **주문 서비스(Order Service) 리뉴얼 프로젝트**


---

## 프로젝트 소개

본 프로젝트는 2026년 1월 5일부터 2026년 2월 26일까지 부트캠프에서 진행한 GutJJeu 프로젝트에서 제가 담당했던 **주문 서비스**를 분리해, 개인적으로 개선 및 리뉴얼하기 위해 진행하는 프로젝트입니다.

원본 프로젝트는 여러 서비스로 구성된 MSA 구조라, 주문 서비스만 떼어내 단독으로 실행 · 테스트할 수 있는 환경을 먼저 구성했습니다. 이후 원본에서 구현하지 못했거나 돌아보며 더 나은 방법이 있다고 판단한 부분을 하나씩 개선하고, 그 내용을 아래 [리뉴얼 기록](#리뉴얼-기록)에 남깁니다.


## 프로젝트 정보

<img width="128" height="128" alt="Image" src="https://github.com/user-attachments/assets/4653b880-a8b5-4542-9447-a17ead852646" />

- 프로젝트명: GutJJeu
- 프로젝트 유형: 개인 리뉴얼 프로젝트
- 원본 프로젝트 기간: 2026.01.05 ~ 2026.02.26
- 원본 담당 파트: 주문 서비스 (주문 · 장바구니 · 대기열)

## 관련 저장소
- 원본 프로젝트: **[beadv3_3_Lets_Co_BE](https://github.com/prgrms-be-adv-devcourse/beadv3_3_Lets_Co_BE)**


---

### ERD Diagram
<img width="463" height="672" alt="OrderERD" src="https://github.com/user-attachments/assets/e39d883a-69c1-4ff7-8b03-b9d4135d61a8" />


---

## 주요 기능

**주문**
- 주문 생성 → 결제 → 결제 후처리 3단계 주문 흐름
- Redis 재고 선차감으로 초과 판매 방지 (단건 Lua Script, 다건 DECR + 보상 롤백)
- 판매자별 정산금을 Redis에 임시 저장 후 결제 성공 시 DB 저장
- 결제 성공 시 Kafka로 상품 서비스에 재고 갱신 이벤트 발행 (커밋 이후 발행)

**장바구니**
- Redis Hash 기반 장바구니 담기 · 수량 변경 · 조회 · 삭제 (TTL 30일)

**대기열**
- Redis ZSet 기반 입장 대기열(유량 제어)과 주문 대기열(동시 주문 인원 제한)
- 실시간 대기 순번 조회, 무반응 사용자 자동 제거


---

## 리뉴얼 기록

### 원본 대비 변경 사항

| 구분 | 내용 |
|---|---|
| 단독 실행 환경 | Eureka 등 MSA 의존성을 걷어내고 주문 서비스만 단독으로 빌드 · 실행되도록 수정 |
| 범위 정리 | 원본에서 다른 팀원이 담당한 정산 배치(Spring Batch) 제외 |
| 테스트 환경 | `loadtest` 프로필에서 상품 서비스 대신 Stub 클라이언트를 사용하도록 구성, MariaDB는 Docker Compose로 실행 |
| 부하 테스트 | DB 비관적 락 방식의 재고 차감을 비교용으로 추가하고, k6 스크립트와 결과를 `loadtest/`에 정리 |

### 개선 예정
- 주문
  - [ ] 다건 주문 재고 차감을 하나의 Lua Script로 개선
  - [ ] 결제 후처리(주문 서비스) 실패 시 재시도 및 실패 건 재처리
  - [ ] Outbox 패턴으로 이벤트 발행 실패 · 유실 방지
- 테스트
  - [ ] (JUnit5) 비즈니스 로직 테스트
  - [ ] (k6) 다건 주문(장바구니)
  - [ ] (k6) 다건 Lua 개선 전후 비교
- 대기열
  - [ ] 대기열 순번 조회를 폴링에서 SSE 기반 서버 푸시로 개선
  - [ ] 주문 대기열 입장을 자리가 빌 때 즉시 처리하는 이벤트 방식으로 개선
  - [ ] 다중 인스턴스 환경에서 스케줄러 중복 실행 방지
- 멱등성
  - [ ] Idempotency-Key로 중복 주문 생성 요청 차단


---

### 기술 스택

#### Language
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)

#### Framework & Library
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/SpringDataJPA-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white)
![OpenFeign](https://img.shields.io/badge/OpenFeign-00CCFF?style=for-the-badge&logo=apache-feign&logoColor=white)

#### Database & Cache
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)

#### Messaging
![Kafka](https://img.shields.io/badge/Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)

#### Infra & Test
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![k6](https://img.shields.io/badge/k6-7D64FF?style=for-the-badge&logo=k6&logoColor=white)
