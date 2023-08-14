package mm.com.mytelpay.adapter.common.webapp.security;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.regex.Pattern;

@Slf4j
@Component
public class RegexPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(
            Authentication authentication, Object targetDomainObject, Object permission) {
        String requiredPermission = permission.toString();
        return authentication.getAuthorities().stream()
                .anyMatch(p -> Pattern.matches(p.getAuthority(), requiredPermission));
    }

    @Override
    public boolean hasPermission(
            Authentication authentication,
            Serializable targetId,
            String targetType,
            Object permission) {
        return hasPermission(authentication, null, permission);
    }
}
