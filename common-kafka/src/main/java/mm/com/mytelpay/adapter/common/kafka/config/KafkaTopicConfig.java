package mm.com.mytelpay.adapter.common.kafka.config;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaAdmin adminClient() {
        Map<String, Object> configs = new HashMap<String, Object>();
        configs.put(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                this.kafkaProperties.getBootstrapServer());
        return new KafkaAdmin(configs);
    }
}
