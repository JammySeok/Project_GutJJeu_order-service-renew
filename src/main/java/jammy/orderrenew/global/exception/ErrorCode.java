package jammy.orderrenew.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400 Bad Request
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE", "입력값이 올바르지 않습니다."),
    NO_INPUT_ADDRESS_DATA(HttpStatus.BAD_REQUEST, "NO_INPUT_ADDRESS_DATA", "주소 정보를 입력해주세요."),
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "PAYMENT_FAILED", "결제를 실패했습니다."),

    // 404 Not Found
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "PAGE_NOT_FOUND", "페이지를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "주문 정보를 찾을 수 없습니다"),
    ORDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_ITEM_NOT_FOUND", "주문 아이템 정보를 찾을 수 없습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_NOT_FOUND", "장바구니 정보를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "제품을 찾을 수 없습니다."),
    SETTLEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "SETTLEMENT_NOT_FOUND", "정산 정보를 찾을 수 없습니다."),

    // 409 Conflict
    OUT_OF_STOCK(HttpStatus.CONFLICT, "OUT_OF_STOCK", "재고가 부족합니다."),

    // 500 Internal Server Error
    ORDER_REFUND_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "ORDER_REFUND_EXCEPTION","주문 후처리 중 오류가 발생하여 자동 환불되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500_INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;  // HTTP 상태 코드
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
}