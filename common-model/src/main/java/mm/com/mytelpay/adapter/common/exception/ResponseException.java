package mm.com.mytelpay.adapter.common.exception;

import mm.com.mytelpay.adapter.common.error.ResponseError;

import java.text.MessageFormat;

public class ResponseException extends RuntimeException {
    private ResponseError error;
    private Object[] params;
    private String refGatewayId;

    public ResponseException(ResponseError error) {
        this(error.getMessage(), null, error);
    }

    public ResponseException(String message, ResponseError error) {
        this(message, null, error);
    }

    public ResponseException(String message, Throwable cause, ResponseError error) {
        this(message, cause, error, null);
    }

    public ResponseException(String message, ResponseError error, Object... params) {
        this(message, null, error, params);
    }

    public ResponseException(
            String message, Throwable cause, ResponseError error, Object... params) {
        super(MessageFormat.format(message, params), cause);
        this.error = error;
        this.params = params == null ? new Object[0] : params;
    }

    public String getRefGatewayId() {
        return refGatewayId;
    }

    public void setRefGatewayId(String refGatewayId) {
        this.refGatewayId = refGatewayId;
    }

    public ResponseError getError() {
        return this.error;
    }

    public void setError(final ResponseError error) {
        this.error = error;
    }

    public Object[] getParams() {
        return this.params;
    }

    public void setParams(final Object[] params) {
        this.params = params;
    }
}
