package com.cozisoft.starter.business;

import com.cozisoft.starter.shared.test.DbResetService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@TestConfiguration
public class CoreTestConfiguration {

    @Bean
    public StarterRestTestClient starterRestTestClient(MockMvc mockMvc, JsonMapper objectMapper) {
        return new StarterRestTestClient(mockMvc, objectMapper);
    }

    @Bean
    public DbResetService dbResetService(JdbcTemplate jdbcTemplate) {
        return new DbResetService(jdbcTemplate);
    }

    @Bean
    public FixtureProvider fixtureProvider(JsonMapper objectMapper) {
        return new FixtureProvider(objectMapper);
    }
}
