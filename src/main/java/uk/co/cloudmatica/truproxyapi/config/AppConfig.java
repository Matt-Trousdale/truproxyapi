package uk.co.cloudmatica.truproxyapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.cloudmatica.truproxyapi.repo.CompanyRepoRemote;
import uk.co.cloudmatica.truproxyapi.service.ProxyService;

@Configuration
public class AppConfig {

    @Bean
    ProxyService proxyService(final CompanyRepoRemote companyRepoRemote) {

        return new ProxyService(companyRepoRemote);
    }
}
