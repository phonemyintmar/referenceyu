package mm.com.mytelpay.adapter.common.api.exception;

public class ErrorCommon extends RuntimeException {
    private String errorCode;
    private String message;

    public ErrorCommon(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
