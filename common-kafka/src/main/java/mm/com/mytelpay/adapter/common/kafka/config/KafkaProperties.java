package mm.com.mytelpay.adapter.common.kafka.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private String bootstrapServer;

    private String consumerGroupId;

    private String acks;

    private String consumerEnableAutoCommit;

    private String consumerAutoCommitIntervalMS;

    private String producerMaxRequestSize;

    private String consumerFetchMaxBytes;

    private String nodeName;
}
