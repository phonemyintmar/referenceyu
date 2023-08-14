package mm.com.mytelpay.adapter.common.webapp.security;

import lombok.extern.slf4j.Slf4j;

import mm.com.mytelpay.adapter.common.ClientDTO;
import mm.com.mytelpay.adapter.common.util.StrUtils;
import mm.com.mytelpay.adapter.common.util.StringPool;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(
            @Lazy AuthorityService authorityService, PasswordEncoder passwordEncoder) {
        this.authorityService = authorityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        ClientDTO clientDTO = authorityService.getClientById(authentication.getName());

        if (clientDTO != null
                && passwordEncoder.matches(
                        authentication.getCredentials().toString(), clientDTO.getSecret())) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (!StrUtils.isBlank(clientDTO.getScope())) {
                List<String> grantedPermissions =
                        List.of(clientDTO.getScope().split(StringPool.COMMA));
                grantedPermissions.forEach(
                        permission -> authorities.add(new SimpleGrantedAuthority(permission)));
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            authentication.getPrincipal(),
                            authentication.getCredentials(),
                            authorities);
            log.info("authentication {}", authenticationToken);
            return authenticationToken;
        }
        throw new BadCredentialsException("The presented client does not contain the expected key");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
