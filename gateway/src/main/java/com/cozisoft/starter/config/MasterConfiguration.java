package com.cozisoft.starter.config;

import com.cozisoft.starter.business.config.GatewayConfiguration;
import com.cozisoft.starter.shared.config.KeycloakProperties;
import com.cozisoft.starter.shared.config.ServiceConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAsync
@EnableWebMvc
@EnableMethodSecurity
@Import({
        AsyncConfiguration.class,
        SecurityConfiguration.class,
        GatewayConfiguration.class
})
@EnableConfigurationProperties({
        KeycloakProperties.class,
        SystemConfigurationProperties.class,
        ServiceConfigurationProperties.class,
})
public class MasterConfiguration {
}
