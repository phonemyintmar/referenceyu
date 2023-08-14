package mm.com.mytelpay.adapter.common.webapp.security;

import lombok.extern.slf4j.Slf4j;

import mm.com.mytelpay.adapter.common.webapp.config.SpringSecurityAuditorAware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Slf4j
@Configuration
@EnableWebSecurity
@Import(SecurityProblemSupport.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class HttpSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String API_PUBLIC_PATTERN = "/api/public/**";
    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;
    private final CustomAuthenticationProvider authProvider;

    public HttpSecurityConfiguration(
            CorsFilter corsFilter,
            SecurityProblemSupport problemSupport,
            @Lazy CustomAuthenticationProvider authProvider) {
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.authProvider = authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/js/*.{js,html}")
                .antMatchers("/i18n/**")
                .antMatchers("/content/**")
                .antMatchers(HttpMethod.GET, API_PUBLIC_PATTERN)
                .antMatchers(HttpMethod.POST, API_PUBLIC_PATTERN)
                .antMatchers("/swagger-ui/index.html");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf()
                .disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(API_PUBLIC_PATTERN)
                .permitAll()
                .antMatchers("/api/files/download-ios-app")
                .permitAll()
                .antMatchers("/api/certificate/.well-known/jwks.json")
                .permitAll()
                .antMatchers("/api/authenticate")
                .permitAll()
                .antMatchers("/api/refresh-token")
                .permitAll()
                .antMatchers("/api/authenticate/**")
                .permitAll()
                .antMatchers("/api/register")
                .permitAll()
                .antMatchers("/api/activate")
                .permitAll()
                .antMatchers("/api/account/reset-password/init")
                .permitAll()
                .antMatchers("/api/account/reset-password/confirm")
                .permitAll()
                .antMatchers("/api/account/reset-password/finish")
                .permitAll()
                .antMatchers("/api/client/authenticate")
                .permitAll()
                .antMatchers("/swagger-*/**")
                .permitAll()
                .antMatchers("/v2/api-docs")
                .permitAll()
                .antMatchers("/api/**")
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuditorAware<String> springSecurityAuditorAware() {
        return new SpringSecurityAuditorAware();
    }
}
