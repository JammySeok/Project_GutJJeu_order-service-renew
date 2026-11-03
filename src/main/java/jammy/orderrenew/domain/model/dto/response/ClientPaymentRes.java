package jammy.orderrenew.domain.model.dto.response;

import jammy.orderrenew.domain.model.enums.PaymentStatus;
import jammy.orderrenew.domain.model.enums.PaymentType;

import java.math.BigDecimal;

/*
 * return 받을 결제 정보
 * @param paymentIdx: 결제 인덱스
 * @param status: 결제 상태 (CHARGE, PAYMENT, REFUND)
 * @param type: 결제 타입 (DEPOSIT, TOSS_PAY, CARD)
 * @param amount: 결제 금액
 * @param cardIdx: 카드 인덱스 (카드 결제 시)
 * @param paymentKey: 토스 페이먼츠 key (토스 페이먼츠 결제 시)
 */
public record ClientPaymentRes(
        Long paymentIdx,
        PaymentStatus status,
        PaymentType type,
        BigDecimal amount,
        Long cardIdx,
        String paymentKey
) {}
