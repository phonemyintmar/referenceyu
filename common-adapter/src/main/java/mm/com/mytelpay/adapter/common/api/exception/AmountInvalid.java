package mm.com.mytelpay.adapter.common.api.exception;

public class AmountInvalid extends ErrorCommon {

    public AmountInvalid() {
        super(ErrorCode.AMOUNT_INVALID.name(), "parameter.invalid.amount");
    }
}
