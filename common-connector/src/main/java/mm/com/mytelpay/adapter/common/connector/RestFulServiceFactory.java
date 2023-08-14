package mm.com.mytelpay.adapter.common.connector;

import com.fasterxml.jackson.databind.ObjectMapper;

import mm.com.mytelpay.adapter.common.connector.httpcalling.CommonServiceGenerator;
import mm.com.mytelpay.adapter.common.connector.httpcalling.HttpConfig;

public class RestFulServiceFactory extends CommonServiceGenerator {

    public RestFulServiceFactory(HttpConfig config, ObjectMapper objectMapper) {
        super(objectMapper);
        setHttpConfig(config);
        retrofitService = buildRetrofit(false);
        retrofitAccess = buildRetrofit(true);
    }
}
