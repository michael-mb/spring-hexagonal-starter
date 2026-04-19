package com.cozisoft.starter.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.liquibase.autoconfigure.LiquibaseProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "system")
public class SystemConfigurationProperties {
    @JsonProperty("liquibase")
    private LiquibaseProperties liquibase;
}
