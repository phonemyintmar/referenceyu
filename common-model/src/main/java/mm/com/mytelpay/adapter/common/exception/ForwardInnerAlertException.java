package mm.com.mytelpay.adapter.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import mm.com.mytelpay.adapter.common.dto.error.ErrorResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class ForwardInnerAlertException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ErrorResponse<Void> response;

    public ForwardInnerAlertException(ErrorResponse<Void> response) {
        this.response = response;
    }
}
