package mm.com.mytelpay.adapter.common.error;

public enum InternalServerError implements ResponseError {
    INTERNAL_SERVER_ERROR(50000001, "There are somethings wrong"),
    DATA_ACCESS_EXCEPTION(50000002, "Data access exception"),
    BAD_GATEWAY(50000003, "Bad gateway"),
    CONNECTION_TIMEOUT(50000004, "Connection timeout"),
    JSON_PARSE_EXCEPTION(50000005, "Json parse exception"),
    ;

    private final Integer code;
    private final String message;

    InternalServerError(Integer code, String message) {
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
        return 500;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
