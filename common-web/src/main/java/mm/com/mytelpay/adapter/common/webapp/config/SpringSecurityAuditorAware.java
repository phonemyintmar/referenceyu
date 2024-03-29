package mm.com.mytelpay.adapter.common.webapp.config;

import mm.com.mytelpay.adapter.common.util.Constants;
import mm.com.mytelpay.adapter.common.webapp.support.SecurityUtils;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

import java.util.Optional;

/** Implementation of {@link AuditorAware} based on Spring Security. */
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUser().orElse(Constants.ANONYMOUS_ACCOUNT));
    }
}
