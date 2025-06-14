# 👟 Sneakers Commerce API

## 🔍 Overview

## 핵심 아키텍처 개요

### ✅ 도메인 역할 분리

- **User (Dtype: ADMIN, SELLER, BUYER)**
  - 단일 테이블에 저장되며, 역할에 따라 기능/권한 분리
  - *판매자(Seller)**는 **쿠폰 발급 권한** 보유
- **Coupon 도메인**
  - 판매자 전용 발급 정책 존재 (정률/정액 할인, 유효기간, 수량 제한)
  - **선착순 쿠폰 발급 시 Redis 기반 동시성 제어 적용**

---

### ✅ 주문 및 결제 흐름

- 주문 생성 시: **상품 재고** 및 **쿠폰 유효성** 확인
- 결제 진행 시: 충전된 잔액 기반, 성공 시
  - `잔액 차감`
  - `결제 정보 저장`
  - `주문 상태 CONFIRMED로 변경`
  - `외부 전송 이벤트 발행`

    → 모두 **트랜잭션 내에서 원자적으로 처리**

- 외부 전송은 **트랜잭셔널 아웃박스 패턴**을 적용하여

  **ORDER_EVENTS 테이블(PENDING)** → 비동기 전송


---

### ✅ 트랜잭셔널 아웃박스 패턴 적용

- 도메인 이벤트(OrderCreated, PaymentCompleted 등)는 `OutboxService`를 통해 저장
- `EventRelayScheduler`가 주기적으로 미전송 이벤트를 외부 플랫폼으로 전송
- 외부 전송 실패 시 `RETRY`, `ALERT` 등으로 상태 전이
- 이를 통해 **DB 트랜잭션 일관성과 외부 연동 실패 복원력 확보**

---

## 📌 기능 요약 및 주요 API

| 기능 | 설명 |
| --- | --- |
| 잔액 충전/조회 | 사용자 잔액을 충전하거나 조회 |
| 상품 조회 | 전체 상품 목록 또는 상세 정보 조회 |
| 주문 생성 | 재고 및 쿠폰 검증 후 주문 생성 |
| 결제 처리 | 잔액 차감 → 결제 승인 → 이벤트 전송 |
| 쿠폰 발급/사용 | 판매자 발급, 사용자 발급/적용 |
| 인기 상품 조회 | 최근 3일간 판매량 기준 Top 5 제공 |

---

## 🗂️ 프로젝트 디렉토리 구조

```bash
src
├── api               # Swagger 명세 전용 인터페이스
├── domain
│   ├── order         # 주문 도메인
│   ├── payment       # 결제 도메인
│   ├── product       # 상품 도메인
│   ├── coupon        # 쿠폰 도메인
│   ├── user          # 사용자 및 잔액 도메인
├── common            # 공통 응답, 예외 처리, 유틸
├── config            # 설정 관련 (Redis, Swagger 등)
├── external          # 외부 플랫폼 연동 모듈
└── test              # 단위 테스트 및 통합 테스트

```

---

## 🧾 설계 문서 & 다이어그램


| 분류 | 문서                                                                        |
| --- |---------------------------------------------------------------------------|
| 시퀀스 다이어그램 | [`/sequence-diagram`](./docs/sequence-diagram) - 잔액 충전, 주문/결제, 인기 상품 조회 등 |
| 클래스 다이어그램 | [`/class-diagram`](./docs/class-diagram) - 도메인 모델/책임/관계 정리                |
| 상태 다이어그램 | [`/state-diagram`](./docs/state-diagram) - 주문, 결제, 쿠폰, 이벤트 전송 상태 흐름 정의    |
|  ERD | [`/er-diagram`](./docs/er-diagram) - 테이블 구조 및 관계                          |
| 이벤트 스토밍 | [`/event-storming`](./docs/event-storming) - 이벤트 중심 설계 및 프로세스 모델링         |****
