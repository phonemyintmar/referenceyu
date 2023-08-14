package mm.com.mytelpay.adapter.common.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;

@Configuration
@EnableJpaRepositories("mm.com.mytelpay.adapter.*")
@EntityScan("mm.com.mytelpay.adapter.*")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class JpaDatabaseAutoConfiguration implements HibernatePropertiesCustomizer {

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", safeSqlInterceptor());
    }

    private SafeSqlInterceptor safeSqlInterceptor() {
        return new SafeSqlInterceptor();
    }
}
