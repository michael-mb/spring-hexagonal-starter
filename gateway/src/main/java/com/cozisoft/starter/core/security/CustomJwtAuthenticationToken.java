package com.cozisoft.starter.core.security;

import com.cozisoft.starter.shared.core.CustomSecurityContextHolder;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Transient
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {
    private final AppUser principal;

    public CustomJwtAuthenticationToken(AppUser principal) {
        super(principal.getToken(), principal.getAuthorities());
        this.principal = principal;
    }

    @Override
    public AppUser getPrincipal() {
        return this.principal;
    }

    @Override
    public @NonNull Collection<GrantedAuthority> getAuthorities() {
        return this.principal.getAuthorities();
    }

    public boolean isSuperAdmin() {
        return Optional.of(getAuthorities())
                .orElse(Set.of())
                .stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), CustomSecurityContextHolder.SUPER_ADMIN_AUTHORITY));
    }
}
