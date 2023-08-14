package mm.com.mytelpay.adapter.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionFailedException extends RuntimeException {

    private static final long serialVersionUID = -5452214461715126937L;

    private String errorCode;

    private String errorMessage;

}
