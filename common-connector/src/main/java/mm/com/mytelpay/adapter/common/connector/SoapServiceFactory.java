package mm.com.mytelpay.adapter.common.connector;

import com.fasterxml.jackson.databind.ObjectMapper;

import mm.com.mytelpay.adapter.common.connector.httpcalling.HttpConfig;
import mm.com.mytelpay.adapter.common.connector.httpcalling.SoapServiceGenerator;

public class SoapServiceFactory extends SoapServiceGenerator {
    private final ObjectMapper objectMapper;

    public SoapServiceFactory(HttpConfig config) {
        objectMapper = new ObjectMapper();
        setHttpConfig(config);
        retrofitService = buildRetrofit(false);
        retrofitAccess = buildRetrofit(true);
    }
}
