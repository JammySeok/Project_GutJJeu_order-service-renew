package jammy.orderrenew.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jammy.orderrenew.model.dto.ProductInfo;
import jammy.orderrenew.model.vo.OrderType;

/*
 * 주문 요청 정보
 * @param orderType: 주문 타입 (Direct, CART)
 * @param productInfo: 제품 정보
 * @param addressInfo: 주소 정보
 * @param cardInfo: 카드 정보
 * @param paymentType: 결제 정보 (DEPOSIT, TOSS_PAY, CARD)
 * @param tossKey: 결제 Key (토스 페이먼츠)
 */
public record OrderReq (

        @Valid
        @NotNull(message = "주문타입은 필수입니다.")
        OrderType orderType,

        ProductInfo productInfo
) {}
