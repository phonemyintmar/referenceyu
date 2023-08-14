package mm.com.mytelpay.adapter.common.api.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import mm.com.mytelpay.adapter.common.api.database.entity.RequestLogEntity;
import mm.com.mytelpay.adapter.common.api.utils.ErrorCodeClient;
import mm.com.mytelpay.adapter.common.dto.request.BaseRequest;
import mm.com.mytelpay.adapter.common.dto.response.BaseResponse;
import mm.com.mytelpay.adapter.common.error.AuthenticationError;
import mm.com.mytelpay.adapter.common.error.BadRequestError;
import mm.com.mytelpay.adapter.common.error.InternalServerError;
import mm.com.mytelpay.adapter.common.exception.ResponseException;
import mm.com.mytelpay.adapter.common.util.StringPool;
import mm.com.mytelpay.adapter.common.webapp.support.SecurityUtils;

import okhttp3.ResponseBody;

import okio.Buffer;
import okio.BufferedSource;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public abstract class AbstractIntegrationBusinessImpl<I extends BaseRequest<?>, O, I2, O2>
        implements AbstractIntegrationBusiness<I, O, I2, O2> {

    private final RequestLogService requestLogService;
    private final ObjectMapper objectMapper;

    protected AbstractIntegrationBusinessImpl(
            RequestLogService requestLogService, ObjectMapper objectMapper) {
        this.requestLogService = requestLogService;
        this.objectMapper = objectMapper;
    }

    protected RequestLogEntity createRequestLog(I request) {
        String clientId =
                SecurityUtils.getCurrentUser()
                        .orElseThrow(() -> new ResponseException(AuthenticationError.UNAUTHORISED));

        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();

        String requestUri = httpServletRequest.getRequestURI();
        String source = httpServletRequest.getRemoteAddr();

        ResponseException exception = null;
        String partnerCode = StringPool.BLANK;
        String serviceCode = StringPool.BLANK;
        String refTransId = StringPool.BLANK;
        String refProductInquiryId = StringPool.BLANK;
        try {
            partnerCode = this.getPartnerCode(request);
        } catch (ResponseException e) {
            exception = e;
        } catch (Exception e) {
            exception =
                    new ResponseException(
                            e.getMessage(), e, BadRequestError.INVALID_REQUEST, "partnerCode");
        }

        try {
            serviceCode = this.getServiceCode(request);
        } catch (ResponseException e) {
            exception = e;
        } catch (Exception e) {
            exception =
                    new ResponseException(
                            e.getMessage(), e, BadRequestError.INVALID_REQUEST, "serviceCode");
        }

        try {
            refTransId = this.getRefTransId(request);
        } catch (ResponseException e) {
            exception = e;
        } catch (Exception e) {
            exception =
                    new ResponseException(
                            e.getMessage(), e, BadRequestError.INVALID_REQUEST, "refTransId");
        }

        try {
            refProductInquiryId = this.getRefProductInquiryId(request);
        } catch (ResponseException e) {
            exception = e;
        } catch (Exception e) {
            exception =
                    new ResponseException(
                            e.getMessage(),
                            e,
                            BadRequestError.INVALID_REQUEST,
                            "refProductInquiryId");
        }

        RequestLogEntity requestLog =
                new RequestLogEntity(
                        source,
                        getDestination(),
                        clientId,
                        partnerCode,
                        serviceCode,
                        refTransId,
                        HttpMethod.POST.toString(),
                        requestUri,
                        refProductInquiryId);

        if (exception != null) {
            try {
                String clientRequestJson = this.objectMapper.writeValueAsString(request);
                requestLog.enrichClientRequestData(clientRequestJson);
            } catch (Exception e) {
                log.error("Fail to parse object data!!!: {}", e.getMessage());
            }
            requestLog.finishedRequestErr(
                    ErrorCodeClient.FAIL.name(),
                    "fail on process request",
                    exception.getMessage(),
                    null);
            requestLog = this.requestLogService.saveLog(requestLog);
            exception.setRefGatewayId(requestLog.getId().toString());
            throw exception;
        }
        return this.requestLogService.saveLog(requestLog);
    }

    @Override
    public BaseResponse<O> onProcess(I request) {
        BaseResponse<O> baseResponse = this.generateDefaultResponse(null);
        RequestLogEntity requestLog = this.createRequestLog(request);

        baseResponse.setRefGatewayId(String.valueOf(requestLog.getId()));
        Integer httpCode = null;
        baseResponse.setResponseTime(LocalDateTime.now());
        try {
            Call<ResponseBody> responseBodyCall = this.buildRequest(request, requestLog);
            // thực hiện request
            requestLog.setPartnerUri(responseBodyCall.request().url().toString());
            requestLog.setMethod(responseBodyCall.request().method());
            Response<ResponseBody> response = responseBodyCall.execute();
            httpCode = response.code();
            String partnerResponseData = this.extractRawResponse(response);
            if (response.isSuccessful()
                    && Objects.nonNull(response.body())
                    && this.checkResponse(partnerResponseData)) {
                // hoàn thành request thêm response
                this.parseResponse(baseResponse, response, requestLog);
                String responseDataClient = this.objectMapper.writeValueAsString(baseResponse);
                requestLog.finishedRequestSuccess(
                        responseDataClient, ErrorCodeClient.SUCCESS.name(), httpCode);
            } else {
                // request thất bại -> check loại exception ->lưu lại request log vào database và
                // trả về response fail
                log.error("Failed on process request {}", partnerResponseData);
                baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
                baseResponse.setMessage(this.getErrorMessage(partnerResponseData));
                requestLog.finishedRequestErr(
                        ErrorCodeClient.FAIL.name(),
                        "Failed on process request",
                        partnerResponseData,
                        httpCode);
                requestLog.enrichPartnerResponseData(partnerResponseData);
            }
        } catch (ConnectException connectException) {
            log.error("Can not init connection to partner!!!: {}", connectException.getMessage());
            baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
            baseResponse.setMessage("Can not connect to partner");
            requestLog.finishedRequestErr(
                    ErrorCodeClient.FAIL.name(),
                    "Can not init connection to partner",
                    connectException.getMessage(),
                    HttpStatus.BAD_GATEWAY.value());
        } catch (SocketTimeoutException socketTimeoutException) {
            log.error("Connect to partner timeout, socket timeout", socketTimeoutException);
            baseResponse.setErrorCode(ErrorCodeClient.TIMEOUT.name());
            baseResponse.setMessage("Connect to partner timeout");
            requestLog.finishedRequestErr(
                    ErrorCodeClient.TIMEOUT.name(),
                    "Connect to partner timeout",
                    socketTimeoutException.getMessage(),
                    HttpStatus.GATEWAY_TIMEOUT.value());
        } catch (JsonProcessingException ex) {
            log.error("This adapter has problem: Please check!!!: {}", ex.getMessage());
            baseResponse.setErrorCode(ErrorCodeClient.UNKNOWN.name());
            baseResponse.setMessage(ex.getMessage());
            requestLog.finishedRequestErr(
                    ErrorCodeClient.UNKNOWN.name(),
                    "Fail to parse object data",
                    ex.getMessage(),
                    httpCode);
        } catch (Exception ex) {
            log.error("This adapter has problem: Please check!!!: {}", ex.getMessage());
            ErrorCodeClient errorCodeClient = ErrorCodeClient.UNKNOWN;
            if (ex instanceof ResponseException) {
                errorCodeClient = ErrorCodeClient.FAIL;
                ResponseException responseException = (ResponseException) ex;
                if (responseException.getError().equals(InternalServerError.CONNECTION_TIMEOUT)) {
                    errorCodeClient = ErrorCodeClient.TIMEOUT;
                    httpCode = HttpStatus.GATEWAY_TIMEOUT.value();
                }
                if (responseException.getError().equals(InternalServerError.BAD_GATEWAY)) {
                    errorCodeClient = ErrorCodeClient.FAIL;
                    httpCode = HttpStatus.BAD_GATEWAY.value();
                }
                if (responseException.getError().equals(BadRequestError.OTP_WRONG)) {
                    errorCodeClient = ErrorCodeClient.OTP_WRONG;
                    httpCode = HttpStatus.BAD_REQUEST.value();
                }
                if (responseException.getError().equals(BadRequestError.OTP_EXPIRED)) {
                    errorCodeClient = ErrorCodeClient.OTP_EXPIRED;
                    httpCode = HttpStatus.BAD_REQUEST.value();
                }
            }
            baseResponse.setErrorCode(errorCodeClient.name());
            baseResponse.setMessage(ex.getMessage());
            requestLog.finishedRequestErr(
                    errorCodeClient.name(), "Fail on process request", ex.getMessage(), httpCode);
        }
        try {
            requestLog.enrichClientResponseData(this.objectMapper.writeValueAsString(baseResponse));
        } catch (JsonProcessingException e) {
            log.error("Client response convert err: Please check!!!: {}", e.getMessage());
        }
        // handler err end
        this.requestLogService.updateLog(requestLog.getId(), requestLog);
        return baseResponse;
    }

    @Override
    public Call<ResponseBody> buildRequest(I req, RequestLogEntity requestLog)
            throws JsonProcessingException {
        String clientRequestJson = this.objectMapper.writeValueAsString(req);
        requestLog.enrichClientRequestData(clientRequestJson);
        I2 partnerRequest = mapRequest(req);
        String partnerRequestJson = this.objectMapper.writeValueAsString(partnerRequest);
        requestLog.enrichPartnerRequestData(partnerRequestJson);
        return createPartnerRequest(partnerRequest);
    }

    @Override
    public boolean checkResponse(String rawResponse) {
        return true;
    }

    @Override
    public String getErrorMessage(String rawResponse) {
        return DEFAULT_ERROR_MESSAGE;
    }

    private String extractRawResponse(Response<ResponseBody> response) throws IOException {
        String partnerResponseData = StringPool.BLANK;
        BufferedSource source = null;
        if (Objects.nonNull(response.errorBody())) {
            source = response.errorBody().source();
        } else if (Objects.nonNull(response.body())) {
            source = response.body().source();
        }
        if (Objects.nonNull(source)) {
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer().clone();
            partnerResponseData = buffer.readString(StandardCharsets.UTF_8);
        }
        return partnerResponseData;
    }

    @Override
    public String getRefProductInquiryId(I request) {
        return null;
    }
}
