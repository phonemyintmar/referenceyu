package mm.com.mytelpay.adapter.common.error;

public enum ServiceUnavailableError implements ResponseError {
    SERVICE_UNAVAILABLE_ERROR(50300001, "Service unavailable"),
    IAM_SERVICE_UNAVAILABLE_ERROR(50300002, "IAM Service unavailable"),
    BUILDING_SERVICE_UNAVAILABLE_ERROR(50300003, "Building Service unavailable"),
    STORAGE_SERVICE_UNAVAILABLE_ERROR(50300004, "Storage Service unavailable"),
    NOTIFICATION_SERVICE_UNAVAILABLE_ERROR(50300005, "Notification Service unavailable"),
    ;

    private final Integer code;
    private final String message;

    ServiceUnavailableError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return 503;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
