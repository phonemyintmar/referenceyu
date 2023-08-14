package mm.com.mytelpay.adapter.common.api.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import mm.com.mytelpay.adapter.common.validator.ValidateConstraint;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "REQUEST_LOG")
public class RequestLogEntity implements Serializable {

    private static final long serialVersionUID = -1198205119417232732L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // source ip
    @Column(name = "SOURCE", length = ValidateConstraint.LENGTH.HOSTNAME_MAX_LENGTH)
    private String source;

    // destination ip
    @Column(name = "DESTINATION", length = ValidateConstraint.LENGTH.HOSTNAME_MAX_LENGTH)
    private String destination;

    @Column(name = "CLIENT_ID", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    private String clientId;

    @Column(name = "PARTNER_CODE", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH)
    private String partnerCode;

    @Column(name = "SERVICE_CODE", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH)
    private String serviceCode;

    @Column(name = "REF_TRANS_ID", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    private String refTransId;

    @Column(name = "METHOD", length = ValidateConstraint.LENGTH.METHOD_MAX_LENGTH)
    private String method;

    @Column(name = "REQUEST_URI", length = ValidateConstraint.LENGTH.URL_MAX_LENGTH)
    private String requestUri;

    @Lob
    @Column(name = "PARTNER_URI")
    private String partnerUri;

    @Column(name = "DURATION", length = 19)
    private Long duration;

    //    Data Business
    @Lob
    @Column(name = "REQUEST_DATA")
    private String requestData;

    @Lob
    @Column(name = "RESPONSE_DATA")
    private String responseData;

    //	 Data partner trả về
    @Lob
    @Column(name = "PARTNER_REQUEST_DATA")
    private String partnerRequestData;

    @Lob
    @Column(name = "PARTNER_RESPONSE_DATA")
    private String partnerResponseData;

    @Column(name = "REQUEST_TIME")
    private Instant requestTime;

    @Column(name = "RESPONSE_TIME")
    private Instant responseTime;

    @Column(name = "HTTP_CODE", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH)
    private Integer httpCode;

    @Column(name = "ERROR_CODE", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH)
    private String errorCode;

    @Lob
    @Column(name = "ERROR_DESCRIPTION")
    private String errorDescription;

    @Lob
    @Column(name = "ERROR_DETAIL")
    private String errorDetail;

    @Column(name = "REF_PRODUCT_INQUIRY_ID", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    private String refProductInquiryId;

    // create request log

    public RequestLogEntity(
            String source,
            String destination,
            String clientId,
            String partnerCode,
            String serviceCode,
            String refTransId,
            String method,
            String requestUri,
            String refProductInquiryId) {
        this.source = source;
        this.destination = destination;
        this.clientId = clientId;
        this.partnerCode = partnerCode;
        this.serviceCode = serviceCode;
        this.refTransId = refTransId;
        this.method = method;
        this.requestUri = requestUri;
        this.requestTime = Instant.now();
        this.refProductInquiryId = refProductInquiryId;
    }

    public RequestLogEntity(
            String source,
            String destination,
            String clientId,
            String partnerCode,
            String serviceCode,
            String refTransId,
            String method,
            String requestUri) {
        this.source = source;
        this.destination = destination;
        this.clientId = clientId;
        this.partnerCode = partnerCode;
        this.serviceCode = serviceCode;
        this.refTransId = refTransId;
        this.method = method;
        this.requestUri = requestUri;
        this.requestTime = Instant.now();
    }

    public RequestLogEntity(
            String source, String destination, String clientId, String method, String requestUri) {
        this.source = source;
        this.destination = destination;
        this.clientId = clientId;
        this.method = method;
        this.requestUri = requestUri;
        this.requestTime = Instant.now();
    }

    //	update when finishedRequest
    public void finishedRequestSuccess(String responseData, String errorCode, Integer httpCode) {
        this.responseData = responseData;
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.responseTime = Instant.now();
        if (Objects.nonNull(requestTime)) {
            this.duration = Duration.between(requestTime, responseTime).toMillis();
        }
    }

    public void finishedRequestErr(
            String errorCode, String errorDescription, String errorDetail, Integer httpCode) {
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
        this.httpCode = httpCode;
        this.errorDescription = errorDescription;

        this.responseTime = Instant.now();
        if (Objects.nonNull(requestTime)) {
            this.duration = Duration.between(requestTime, responseTime).toMillis();
        }
    }

    public void enrichPartnerRequestData(String requestData) {
        this.partnerRequestData = requestData;
    }

    public void enrichPartnerResponseData(String responseData) {
        this.partnerResponseData = responseData;
    }

    public void enrichClientRequestData(String requestData) {
        this.requestData = requestData;
    }

    public void enrichClientResponseData(String responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        return "RequestLogEntity{"
                + "id='"
                + id
                + '\''
                + ", source='"
                + source
                + '\''
                + ", destination='"
                + destination
                + '\''
                + ", clientId='"
                + clientId
                + '\''
                + ", partnerCode='"
                + partnerCode
                + '\''
                + ", method='"
                + method
                + '\''
                + ", requestUri='"
                + requestUri
                + '\''
                + ", partnerUri='"
                + partnerUri
                + '\''
                + ", serviceCode='"
                + serviceCode
                + '\''
                + ", duration="
                + duration
                + ", requestData='"
                + requestData
                + '\''
                + ", responseData='"
                + responseData
                + '\''
                + ", partnerRequestData='"
                + partnerRequestData
                + '\''
                + ", partnerResponseData='"
                + partnerResponseData
                + '\''
                + ", requestTime="
                + requestTime
                + ", responseTime="
                + responseTime
                + ", httpCode="
                + httpCode
                + ", errorCode='"
                + errorCode
                + '\''
                + ", errorDescription='"
                + errorDescription
                + '\''
                + ", errorDetail='"
                + errorDetail
                + '\''
                + '}';
    }
}
