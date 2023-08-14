package mm.com.mytelpay.adapter.common.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 5264282734570402387L;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("responseTime")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "Asia/Yangon")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime responseTime;

    @JsonProperty("message")
    private String message;

    @JsonProperty("refGatewayId")
    private String refGatewayId;

    @JsonProperty("result")
    private T result;

    public BaseResponse(String errorCode, String message, T result) {
        this.errorCode = errorCode;
        this.message = message;
        this.responseTime = LocalDateTime.now();
        this.result = result;
    }
}
