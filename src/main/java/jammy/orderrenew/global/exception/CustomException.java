package jammy.orderrenew.global.exception;

public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    // 기본 메시지 사용
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 별도 메시지 사용
    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}