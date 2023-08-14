package mm.com.mytelpay.adapter.common.api.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class AdapterHelper {
    private AdapterHelper() {
        // default constructor
    }

    public static String buildRequestTemplate(String template, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            log.info("{}:{}", entry.getKey(), entry.getValue());
            template = template.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return template.replaceAll("\\r\\n|\\n|\\r", "");
    }
}
