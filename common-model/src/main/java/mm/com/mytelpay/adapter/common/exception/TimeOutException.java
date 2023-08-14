package mm.com.mytelpay.adapter.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TimeOutException extends RuntimeException {

    private static final long serialVersionUID = 8426171780173516525L;

    private String errorCode;

    private String errorMessage;
}
