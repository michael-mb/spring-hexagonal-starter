package com.cozisoft.starter.account.infra.keycloak;

import com.cozisoft.starter.account.core.model.payload.ExternalUserAccountInfo;
import com.cozisoft.starter.account.core.port.out.IdPUserManagementAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class DefaultIdPUserManagementAdapter implements IdPUserManagementAdapter {
    private final CustomKeycloakAdminClient keycloakAdminClient;

    @Override
    public Optional<ExternalUserAccountInfo> getUserById(String userId) {
        log.debug("Getting user info from Keycloak with userId: {}", userId);
        Optional<ExternalUserAccountInfo> user = keycloakAdminClient.getUserById(userId);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        ExternalUserAccountInfo userInfo = user.get();
        Set<String> roles = keycloakAdminClient.getRolesForUser(userId);
        userInfo.setRoles(roles);
        return Optional.of(userInfo);
    }

    @Override
    public ExternalUserAccountInfo createUser(ExternalUserAccountInfo user) {
        log.debug("Creating user in Keycloak with email: {}", user.getEmail());
        return keycloakAdminClient.createUser(user);
    }

    @Override
    public void patchUser(ExternalUserAccountInfo user) {
        log.debug("Patching user in Keycloak with userId: {}", user.getSub());
        keycloakAdminClient.patchUser(user);
    }
}
