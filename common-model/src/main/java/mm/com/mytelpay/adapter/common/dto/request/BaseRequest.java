package mm.com.mytelpay.adapter.common.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseRequest<T> implements Serializable {

    private static final long serialVersionUID = -6441394422005254022L;

    @NotBlank(message = "requestId is required")
    @JsonProperty("requestId")
    private String requestId;

    @NotBlank(message = "clientTime is required")
    @JsonProperty("clientTime")
    private String clientTime;

    @NotBlank(message = "client is required")
    @JsonProperty("client")
    private String client;

    @Valid private T content;
}
