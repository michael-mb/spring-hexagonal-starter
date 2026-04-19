package com.cozisoft.starter.account.core.port.in;

import com.cozisoft.starter.account.core.model.entities.Account;
import com.cozisoft.starter.account.core.model.payload.PatchAccountRequest;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

public interface AccountManagement {
    Account whoami();
    Account createAccount(String idpUserId);
    Account patchAccountById(Long accountId, @Validated PatchAccountRequest request);
    void deleteAccountById(Long accountId);
    Account getAccountByIdpUserId(String idpUserId);
    Optional<Account> getAccountById(Long accountId);
    void suspendAccount(Long accountId);
    void activateAccount(Long accountId);
}
