package com.cozisoft.starter.business;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class FixtureProvider {
    private final JsonMapper objectMapper;

    public <T> T loadAs(String path, Class<T> type) throws IOException {
        return objectMapper.readValue(
                new ClassPathResource("fixtures/" + path).getInputStream(), type);
    }

    public String loadAsString(String path) throws IOException {
        return new ClassPathResource("fixtures/" + path)
                .getContentAsString(StandardCharsets.UTF_8);
    }
}
