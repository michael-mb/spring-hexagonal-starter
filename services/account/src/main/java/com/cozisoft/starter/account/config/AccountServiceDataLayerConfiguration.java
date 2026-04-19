package com.cozisoft.starter.account.config;

import com.cozisoft.starter.shared.config.ServiceConfigurationProperties;
import com.cozisoft.starter.shared.config.ServiceDataLayerBaseConfiguration;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@EnableJpaRepositories(basePackages = "com.cozisoft.starter.account.*")
public class AccountServiceDataLayerConfiguration extends ServiceDataLayerBaseConfiguration {
    protected AccountServiceDataLayerConfiguration(
            @Value("${services.account.name}") String serviceName,
            DataSource dataSource,
            ServiceConfigurationProperties configuration) {
        super(dataSource, configuration.getServiceLiquibaseProperties(serviceName));
    }

    @Bean(name = "accountLiquibaseRunner")
    public SpringLiquibase liquibase() {
        return getSpringLiquibase("accountLiquibaseRunner");
    }
}
