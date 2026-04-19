package com.cozisoft.starter.business.config;

import com.cozisoft.starter.account.core.port.in.AccountManagement;
import com.cozisoft.starter.business.core.AccountManagementFacadeImpl;
import com.cozisoft.starter.business.core.port.in.AccountManagementFacade;
import org.springframework.context.annotation.Bean;

public class GatewayConfiguration {

    @Bean
    public AccountManagementFacade accountManagementFacade(AccountManagement accountManagement) {
        return new AccountManagementFacadeImpl(accountManagement);
    }
}
