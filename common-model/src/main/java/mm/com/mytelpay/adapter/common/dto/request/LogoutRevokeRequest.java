package mm.com.mytelpay.adapter.common.dto.request;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRevokeRequest extends Request {
    private String deviceId;

    private String deviceToken;

    private String userId;
}
