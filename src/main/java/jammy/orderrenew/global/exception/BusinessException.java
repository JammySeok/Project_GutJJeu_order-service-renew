package jammy.orderrenew.global.exception;

// 비즈니스 로직 예외 처리를 위한 커스텀 RuntimeException
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    // ErrorCode의 기본 메시지 사용
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 별도의 커스텀 메시지 사용
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
