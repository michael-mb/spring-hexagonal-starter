package com.cozisoft.starter.business.core.port.in;

import com.cozisoft.starter.account.core.model.entities.Account;
import com.cozisoft.starter.account.core.model.payload.PatchAccountRequest;
import com.cozisoft.starter.business.core.model.UpdateAccountStatusRequest;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

public interface AccountManagementFacade {
    Account whoami();
    Account createAccount();
    Account patchAccountById(Long accountId, @Validated PatchAccountRequest request);
    void deleteAccountById(Long accountId);
    Optional<Account> getAccountById(Long accountId);
    void updateAccountStatusById(Long accountId, UpdateAccountStatusRequest request);
}
