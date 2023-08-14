package mm.com.mytelpay.adapter.common.dto.request;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import mm.com.mytelpay.adapter.common.validator.ValidateUUID;

import java.util.List;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class FindByIdsRequest extends Request {

    @NotEmpty(message = "IDS_REQUIRED")
    private List<@ValidateUUID String> ids;
}
