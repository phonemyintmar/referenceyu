package mm.com.mytelpay.adapter.common.sdk.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "adapter.property")
public class AdapterClientProperty {

    private String url;
    private String clientId;
    private String clientSecret;
    private Long connectionTimeout = 60L;
    private Long readTimeout = 60L;
    private Long writeTimeout = 60L;
}
