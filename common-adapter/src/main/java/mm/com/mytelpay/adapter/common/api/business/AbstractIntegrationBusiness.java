package mm.com.mytelpay.adapter.common.api.business;

import com.fasterxml.jackson.core.JsonProcessingException;

import mm.com.mytelpay.adapter.common.api.database.entity.RequestLogEntity;
import mm.com.mytelpay.adapter.common.dto.response.BaseResponse;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public interface AbstractIntegrationBusiness<I, O, I2, O2> extends BaseBusiness<I, O> {

    String DEFAULT_ERROR_MESSAGE = "Failed to process request";

    String getServiceCode(I request);

    String getPartnerCode(I request);

    String getRefTransId(I request);

    String getDestination();

    I2 mapRequest(I i) throws JsonProcessingException;

    O mapResponse(O2 o);

    BaseResponse<O> onProcess(I request);

    boolean checkResponse(String rawResponse);

    String getErrorMessage(String rawResponse);

    Call<ResponseBody> buildRequest(I req, RequestLogEntity requestLog)
            throws JsonProcessingException;

    Call<ResponseBody> createPartnerRequest(I2 i) throws JsonProcessingException;

    void parseResponse(
            BaseResponse<O> baseResponse,
            Response<ResponseBody> httpResponse,
            RequestLogEntity requestLog)
            throws IOException;

    String getRefProductInquiryId(I request);
}
