package com.cozisoft.starter.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    @SuppressWarnings("unchecked")
    public AbstractAuthenticationToken convert(Jwt source) {
        Map<String, Object> realmAccess = source.getClaimAsMap("realm_access");
        List<String> roleNames = realmAccess != null
                ? (List<String>) realmAccess.getOrDefault("roles", List.of())
                : List.of();

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String role : roleNames) {
            authorities.add(new SimpleGrantedAuthority(mapToInternalRole(role)));
        }

        AppUser principal = AppUser.builder()
                .token(source)
                .authorities(authorities)
                .build();

        return new CustomJwtAuthenticationToken(principal);
    }

    private String mapToInternalRole(String role) {
        return role.startsWith("ROLE_") ? role : "ROLE_%s".formatted(role);
    }
}
