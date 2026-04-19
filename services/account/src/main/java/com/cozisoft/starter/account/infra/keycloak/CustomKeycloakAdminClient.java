package com.cozisoft.starter.account.infra.keycloak;

import com.cozisoft.starter.account.core.model.payload.ExternalUserAccountInfo;
import com.cozisoft.starter.shared.config.KeycloakProperties;
import com.cozisoft.starter.shared.core.errorhandling.ErrorCodes;
import com.cozisoft.starter.shared.core.errorhandling.exceptions.DataAlreadyExistsException;
import com.cozisoft.starter.shared.core.errorhandling.exceptions.DataDoesNotExistException;
import com.cozisoft.starter.shared.core.errorhandling.exceptions.InvalidSubmissionException;
import com.cozisoft.starter.shared.core.errorhandling.exceptions.NoPermissionException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CustomKeycloakAdminClient {

    private final KeycloakProperties properties;
    private final RestClient restClient;

    private volatile String cachedToken;
    private volatile Instant tokenExpiry = Instant.MIN;

    public CustomKeycloakAdminClient(KeycloakProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .baseUrl(properties.getServerUrl())
                .build();
    }

    public Optional<ExternalUserAccountInfo> getUserById(String userId) {
        try {
            KeycloakUser user = restClient.get()
                    .uri("/admin/realms/{realm}/users/{id}", properties.getRealm(), userId)
                    .header("Authorization", "Bearer " + getAdminToken())
                    .retrieve()
                    .body(KeycloakUser.class);
            return Optional.ofNullable(user).map(this::toExternalUserInfo);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (HttpClientErrorException e) {
            throw mapException(e, "Failed to get user %s".formatted(userId));
        }
    }

    public ExternalUserAccountInfo createUser(ExternalUserAccountInfo user) {
        KeycloakUser body = toKeycloakUser(user);
        try {
            var response = restClient.post()
                    .uri("/admin/realms/{realm}/users", properties.getRealm())
                    .header("Authorization", "Bearer " + getAdminToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

            String location = response.getHeaders().getFirst("Location");
            if (location == null) {
                throw new InvalidSubmissionException(ErrorCodes.UNEXPECTED_ERROR, "Keycloak did not return Location header after user creation");
            }
            String createdId = location.substring(location.lastIndexOf('/') + 1);
            return getUserById(createdId)
                    .orElseThrow(() -> new InvalidSubmissionException(ErrorCodes.UNEXPECTED_ERROR, "Could not fetch newly created user %s".formatted(createdId)));
        } catch (HttpClientErrorException e) {
            throw mapException(e, "Failed to create user with email %s".formatted(user.getEmail()));
        }
    }

    public ExternalUserAccountInfo patchUser(ExternalUserAccountInfo user) {
        String userId = user.getSub();
        KeycloakUser body = toKeycloakUser(user);
        try {
            restClient.put()
                    .uri("/admin/realms/{realm}/users/{id}", properties.getRealm(), userId)
                    .header("Authorization", "Bearer " + getAdminToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            return getUserById(userId)
                    .orElseThrow(() -> new DataDoesNotExistException(ErrorCodes.ACCOUNT_NOT_FOUND, "No account found for user %s", userId));
        } catch (HttpClientErrorException e) {
            throw mapException(e, "Failed to patch user %s".formatted(userId));
        }
    }

    public void deleteUser(String userId) {
        try {
            restClient.delete()
                    .uri("/admin/realms/{realm}/users/{id}", properties.getRealm(), userId)
                    .header("Authorization", "Bearer " + getAdminToken())
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            throw mapException(e, "Failed to delete user %s".formatted(userId));
        }
    }

    public void updateUserEnabled(String userId, boolean enabled) {
        KeycloakUser body = new KeycloakUser();
        body.setEnabled(enabled);
        try {
            restClient.put()
                    .uri("/admin/realms/{realm}/users/{id}", properties.getRealm(), userId)
                    .header("Authorization", "Bearer " + getAdminToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            throw mapException(e, "Failed to update enabled=%s for user %s".formatted(enabled, userId));
        }
    }

    public Set<String> getRolesForUser(String userId) {
        try {
            List<KeycloakRole> roles = restClient.get()
                    .uri("/admin/realms/{realm}/users/{id}/role-mappings/realm", properties.getRealm(), userId)
                    .header("Authorization", "Bearer " + getAdminToken())
                    .retrieve()
                    .body(new org.springframework.core.ParameterizedTypeReference<List<KeycloakRole>>() {});
            return roles == null ? Set.of() : roles.stream().map(KeycloakRole::getName).collect(Collectors.toSet());
        } catch (HttpClientErrorException e) {
            throw mapException(e, "Failed to get roles for user %s".formatted(userId));
        }
    }

    public void assignRealmRole(String userId, String roleName) {
        KeycloakRole role = findRealmRole(roleName)
                .orElseThrow(() -> new DataDoesNotExistException(ErrorCodes.INVALID_ROLE, "Role not found: %s".formatted(roleName)));
        try {
            restClient.post()
                    .uri("/admin/realms/{realm}/users/{id}/role-mappings/realm", properties.getRealm(), userId)
                    .header("Authorization", "Bearer " + getAdminToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(List.of(role))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            throw mapException(e, "Failed to assign role %s to user %s".formatted(roleName, userId));
        }
    }

    private Optional<KeycloakRole> findRealmRole(String roleName) {
        try {
            KeycloakRole role = restClient.get()
                    .uri("/admin/realms/{realm}/roles/{name}", properties.getRealm(), roleName)
                    .header("Authorization", "Bearer " + getAdminToken())
                    .retrieve()
                    .body(KeycloakRole.class);
            return Optional.ofNullable(role);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (HttpClientErrorException e) {
            throw mapException(e, "Failed to find role %s".formatted(roleName));
        }
    }

    private synchronized String getAdminToken() {
        if (cachedToken != null && Instant.now().isBefore(tokenExpiry)) {
            return cachedToken;
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", properties.getAdminClientId());
        form.add("username", properties.getAdminUsername());
        form.add("password", properties.getAdminPassword());
        form.add("grant_type", "password");

        TokenResponse token = restClient.post()
                .uri("/realms/master/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(TokenResponse.class);

        if (token == null || token.getAccessToken() == null) {
            throw new InvalidSubmissionException(ErrorCodes.NO_ACCESS_TOKEN, "Failed to obtain Keycloak admin token");
        }

        cachedToken = token.getAccessToken();
        tokenExpiry = Instant.now().plusSeconds(Math.max(token.getExpiresIn() - 10, 30));
        return cachedToken;
    }

    private ExternalUserAccountInfo toExternalUserInfo(KeycloakUser user) {
        return ExternalUserAccountInfo.builder()
                .sub(user.getId())
                .email(user.getEmail())
                .givenName(user.getFirstName())
                .familyName(user.getLastName())
                .emailVerified(Boolean.TRUE.equals(user.getEmailVerified()))
                .blocked(!Boolean.TRUE.equals(user.getEnabled()))
                .build();
    }

    private KeycloakUser toKeycloakUser(ExternalUserAccountInfo info) {
        KeycloakUser user = new KeycloakUser();
        user.setEmail(info.getEmail());
        user.setUsername(info.getEmail());
        user.setFirstName(info.getGivenName());
        user.setLastName(info.getFamilyName());
        user.setEnabled(true);
        user.setEmailVerified(info.isEmailVerified());
        return user;
    }

    private RuntimeException mapException(HttpClientErrorException e, String context) {
        int status = e.getStatusCode().value();
        String message = context + ": " + e.getMessage();
        return switch (status) {
            case 404 -> new DataDoesNotExistException(ErrorCodes.ACCOUNT_NOT_FOUND, message);
            case 409 -> new DataAlreadyExistsException(ErrorCodes.ACCOUNT_ALREADY_EXISTS, message);
            case 401, 403 -> new NoPermissionException(ErrorCodes.NO_ACCESS_TOKEN, message);
            default -> new InvalidSubmissionException(ErrorCodes.UNEXPECTED_ERROR, message);
        };
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KeycloakUser {
        private String id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private Boolean enabled;
        private Boolean emailVerified;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KeycloakRole {
        private String id;
        private String name;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TokenResponse {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("expires_in")
        private int expiresIn;
    }
}
