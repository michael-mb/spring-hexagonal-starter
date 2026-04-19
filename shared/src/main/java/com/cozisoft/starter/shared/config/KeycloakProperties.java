package com.cozisoft.starter.shared.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "system.keycloak")
public class KeycloakProperties {
    private String serverUrl;
    private String realm;
    private String adminClientId;
    private String adminUsername;
    private String adminPassword;
}
