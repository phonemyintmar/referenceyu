package mm.com.mytelpay.adapter.common.error;

import lombok.Getter;

@Getter
public enum BadRequestError implements ResponseError {
    INVALID_INPUT(40000001, "Invalid input : {0}"),
    INVALID_ACCEPT_LANGUAGE(40000002, "Invalid value for request header Accept-Language: {0}"),
    MISSING_PATH_VARIABLE(40000003, "Missing path variable"),
    PATH_INVALID(40000004, "Path is invalid"),
    UNDEFINED(40000005, ""),
    FILE_SIZE_EXCEEDED(40000006, "File size exceeds the limit"),
    RESULT_NOT_FOUND(40000007, "Result not found"),
    METHOD_NOT_ALLOWED(40000009, "Method not allowed"),
    PRODUCT_INQUIRY_NOT_FOUND(40000008, "Product inquiry not found"),
    PRODUCT_NOT_FOUND(40000009, "Product not found"),
    INVALID_REQUEST(400000010, "Invalid request {0}"),
    SERVICE_CODE_INVALID(40000011, "serviceCode invalid"),
    CONTENT_INVALID(40000012, "content invalid"),
    PARTNER_CODE_INVALID(40000013, "partnerCode invalid"),
    REF_TRANS_ID_IS_EXISTS(40000014, "RefTransId is exists"),
    REF_PRODUCT_INQUIRY_ID_IS_EXISTS(40000015, "RefProductInquiryId is exists"),
    OTP_EXPIRED(40000016, "OTP is expired"),
    OTP_WRONG(40000017, "OTP is invalid");

    private final Integer code;
    private final String message;

    BadRequestError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return 400;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
