package mm.com.mytelpay.adapter.common.api.business;

import mm.com.mytelpay.adapter.common.api.utils.ErrorCodeClient;
import mm.com.mytelpay.adapter.common.dto.response.BaseResponse;

public interface BaseBusiness<I, O> {

    default BaseResponse<O> generateDefaultResponse(O res) {
        return BaseResponse.<O>builder()
                .errorCode(ErrorCodeClient.SUCCESS.name())
                .message("Success")
                .build();
    }

    BaseResponse<O> onProcess(I request);
}
