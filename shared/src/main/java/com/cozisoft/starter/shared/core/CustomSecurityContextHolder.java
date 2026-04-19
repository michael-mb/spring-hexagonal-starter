package com.cozisoft.starter.shared.core;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class CustomSecurityContextHolder {

    public static final String SUPER_ADMIN_AUTHORITY = "ROLE_SUPER_ADMIN";
    public static final String ADMIN_AUTHORITY = "ROLE_ADMIN";
    public static final String USER_AUTHORITY = "ROLE_USER";

    @SuppressWarnings("unchecked")
    public Optional<AbstractOAuth2TokenAuthenticationToken<Jwt>> getAuthenticationToken() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AbstractOAuth2TokenAuthenticationToken<?> token)
            return Optional.of((AbstractOAuth2TokenAuthenticationToken<Jwt>) token);
        return Optional.empty();
    }

    public boolean doesAuthenticationHasRole(
            AbstractOAuth2TokenAuthenticationToken<Jwt> authentication, String role) {
        Objects.requireNonNull(authentication, "Authentication must not be null");
        return authentication.getAuthorities()
                .stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), role));
    }

    public Optional<String> getAuthenticatedUsername() {
        return getAuthenticationToken()
                .map(AbstractAuthenticationToken::getName);
    }

    public boolean isSuperAdmin() {
        return getAuthenticationToken()
                .map(auth -> doesAuthenticationHasRole(auth, SUPER_ADMIN_AUTHORITY))
                .orElse(false);
    }

    public boolean isAdmin() {
        return getAuthenticationToken()
                .map(auth -> doesAuthenticationHasRole(auth, ADMIN_AUTHORITY))
                .orElse(false);
    }
}
