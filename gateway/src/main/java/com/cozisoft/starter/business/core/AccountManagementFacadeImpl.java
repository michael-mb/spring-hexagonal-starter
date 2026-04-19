package com.cozisoft.starter.business.core;

import com.cozisoft.starter.account.core.model.entities.Account;
import com.cozisoft.starter.account.core.model.payload.PatchAccountRequest;
import com.cozisoft.starter.account.core.port.in.AccountManagement;
import com.cozisoft.starter.business.core.model.UpdateAccountStatusRequest;
import com.cozisoft.starter.business.core.port.in.AccountManagementFacade;
import com.cozisoft.starter.shared.core.CustomSecurityContextHolder;
import com.cozisoft.starter.shared.core.errorhandling.ErrorCodes;
import com.cozisoft.starter.shared.core.errorhandling.exceptions.DataDoesNotExistException;
import com.cozisoft.starter.shared.core.errorhandling.exceptions.NoPermissionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AccountManagementFacadeImpl implements AccountManagementFacade {
    private final AccountManagement accountManagement;

    @Override
    public Account whoami() {
        return accountManagement.whoami();
    }

    @Override
    public Account createAccount() {
        return accountManagement.createAccount(getAuthenticatedUsername());
    }

    @Override
    public Account patchAccountById(Long accountId, PatchAccountRequest request) {
        Account account = accountManagement.getAccountById(accountId)
                .orElseThrow(() -> new DataDoesNotExistException(ErrorCodes.ACCOUNT_NOT_FOUND,
                        "No account found for id: {}", String.valueOf(accountId)));
        String authenticatedSub = getAuthenticatedUsername();
        if (!account.getIdpUserId().equals(authenticatedSub)) {
            throw new NoPermissionException(ErrorCodes.NO_PERMISSION, "Cannot patch another user's account");
        }
        return accountManagement.patchAccountById(accountId, request);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAccountById(Long accountId) {
        accountManagement.deleteAccountById(accountId);
    }

    @Override
    public Optional<Account> getAccountById(Long accountId) {
        return accountManagement.getAccountById(accountId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateAccountStatusById(Long accountId, UpdateAccountStatusRequest request) {
        switch (request.getAction()) {
            case SUSPEND -> accountManagement.suspendAccount(accountId);
            case ACTIVATE -> accountManagement.activateAccount(accountId);
        }
    }

    private String getAuthenticatedUsername() {
        Optional<String> idpUserId = CustomSecurityContextHolder.getAuthenticatedUsername();
        if (idpUserId.isEmpty()) {
            throw new NoPermissionException(ErrorCodes.NO_ACCESS_TOKEN, "User is not authenticated");
        }
        return idpUserId.get();
    }
}
