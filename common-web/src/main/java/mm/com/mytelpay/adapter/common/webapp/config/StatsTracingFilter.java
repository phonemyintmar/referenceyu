package mm.com.mytelpay.adapter.common.webapp.config;

import lombok.extern.slf4j.Slf4j;

import mm.com.mytelpay.adapter.common.webapp.support.SecurityUtils;

import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/api/**")
@Slf4j
@Profile({"local", "dev", "prod", "uat"})
public class StatsTracingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        Instant start = Instant.now();
        try {
            chain.doFilter(req, resp);
        } finally {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            Optional<String> optionalCurrentUserId = SecurityUtils.getCurrentUser();
            if (optionalCurrentUserId.isEmpty()) {
                log.info(
                        "Execute api: {}, status: {}, time: {} ms ",
                        ((HttpServletRequest) req).getRequestURI(),
                        ((HttpServletResponse) resp).getStatus(),
                        time);
            } else {
                log.info(
                        "User/client: {}, execute api: {}, status: {}, time: {} ms ",
                        optionalCurrentUserId.get(),
                        ((HttpServletRequest) req).getRequestURI(),
                        ((HttpServletResponse) resp).getStatus(),
                        time);
            }
        }
    }
}
