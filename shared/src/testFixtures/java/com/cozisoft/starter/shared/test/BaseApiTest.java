package com.cozisoft.starter.shared.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import tools.jackson.databind.json.JsonMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

public abstract class BaseApiTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JsonMapper objectMapper;

    protected RequestPostProcessor withAuth(String sub, String... roles) {
        var authorities = Arrays.stream(roles)
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();

        var jwt = Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .subject(sub)
                .issuer(TestResourceExtension.getIssuer())
                .claim("realm_access", Map.of("roles", List.of(roles)))
                .build();

        return authentication(new JwtAuthenticationToken(jwt, authorities));
    }
}
