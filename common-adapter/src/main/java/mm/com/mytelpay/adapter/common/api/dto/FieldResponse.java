package mm.com.mytelpay.adapter.common.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldResponse {
    private String name;
    private String value;
}
