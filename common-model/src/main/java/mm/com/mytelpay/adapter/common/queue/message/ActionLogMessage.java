package mm.com.mytelpay.adapter.common.queue.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import mm.com.mytelpay.adapter.common.queue.AuditableMessage;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ActionLogMessage extends AuditableMessage {

    private static final long serialVersionUID = -3374344474737587135L;
    protected Instant accessedAt;
    private String uri;
    private String method;
    private String userId;
    private String remoteIp;
    private String userAgent;
    private String module;
    private String requestPayload;
    private String requestParam;
    private String responsePayload;
    private Integer httpCode;
    private String hostIp;
    private long duration;
}
