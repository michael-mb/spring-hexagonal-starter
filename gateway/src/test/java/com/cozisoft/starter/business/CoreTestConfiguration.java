package com.cozisoft.starter.business;

import com.cozisoft.starter.shared.test.DbResetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@TestConfiguration
public class CoreTestConfiguration {

    @Bean
    public StarterRestTestClient starterRestTestClient(MockMvc mockMvc, ObjectMapper objectMapper) {
        return new StarterRestTestClient(mockMvc, objectMapper);
    }

    @Bean
    public DbResetService dbResetService(JdbcTemplate jdbcTemplate) {
        return new DbResetService(jdbcTemplate);
    }

    @Bean
    public FixtureProvider fixtureProvider(ObjectMapper objectMapper) {
        return new FixtureProvider(objectMapper);
    }
}
