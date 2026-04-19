package com.cozisoft.starter.account.config;

import com.cozisoft.starter.account.core.AccountManager;
import com.cozisoft.starter.account.core.port.in.AccountManagement;
import com.cozisoft.starter.account.core.port.out.AccountRepository;
import com.cozisoft.starter.account.core.port.out.IdPUserManagementAdapter;
import com.cozisoft.starter.account.infra.AccountRepositoryImpl;
import com.cozisoft.starter.account.infra.db.JpaAccountRepository;
import com.cozisoft.starter.account.infra.keycloak.CustomKeycloakAdminClient;
import com.cozisoft.starter.account.infra.keycloak.DefaultIdPUserManagementAdapter;
import com.cozisoft.starter.shared.config.KeycloakProperties;
import org.springframework.context.annotation.Bean;

public class AccountDomainConfiguration {

    @Bean
    public AccountManagement accountManagement(AccountRepository accountRepository, IdPUserManagementAdapter userManagementAdapter) {
        return new AccountManager(accountRepository, userManagementAdapter);
    }

    @Bean
    public AccountRepository accountRepository(JpaAccountRepository jpaAccountRepository) {
        return new AccountRepositoryImpl(jpaAccountRepository);
    }

    @Bean
    public CustomKeycloakAdminClient keycloakAdminClient(KeycloakProperties keycloakProperties) {
        return new CustomKeycloakAdminClient(keycloakProperties);
    }

    @Bean
    public IdPUserManagementAdapter userManagementAdapter(CustomKeycloakAdminClient keycloakAdminClient) {
        return new DefaultIdPUserManagementAdapter(keycloakAdminClient);
    }
}
