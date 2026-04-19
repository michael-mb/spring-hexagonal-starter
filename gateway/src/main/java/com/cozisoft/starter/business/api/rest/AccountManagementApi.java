package com.cozisoft.starter.business.api.rest;

import com.cozisoft.starter.account.core.model.entities.Account;
import com.cozisoft.starter.api.AccountsApi;
import com.cozisoft.starter.business.api.model.AccountMapper;
import com.cozisoft.starter.business.core.port.in.AccountManagementFacade;
import com.cozisoft.starter.dto.AccountDto;
import com.cozisoft.starter.dto.PatchAccountRequestDto;
import com.cozisoft.starter.dto.UpdateAccountStatusRequestDto;
import com.cozisoft.starter.shared.core.errorhandling.ErrorCodes;
import com.cozisoft.starter.shared.core.errorhandling.exceptions.DataDoesNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountManagementApi implements AccountsApi {

    private final AccountManagementFacade accountManagementFacade;
    private final AccountMapper accountMapper;

    @Override
    public ResponseEntity<AccountDto> whoami() {
        return ResponseEntity.ok(accountMapper.map(accountManagementFacade.whoami()));
    }

    @Override
    public ResponseEntity<AccountDto> createAccount() {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                accountMapper.map(accountManagementFacade.createAccount()));
    }

    @Override
    public ResponseEntity<AccountDto> getAccountById(Long accountId) {
        Optional<Account> account = accountManagementFacade.getAccountById(accountId);
        if (account.isEmpty()) {
            throw new DataDoesNotExistException(ErrorCodes.ACCOUNT_NOT_FOUND, "No account found for id: {}", String.valueOf(accountId));
        }
        return ResponseEntity.ok(accountMapper.map(account.get()));
    }

    @Override
    public ResponseEntity<AccountDto> patchAccountById(Long accountId, PatchAccountRequestDto patchAccountRequestDto) {
        return ResponseEntity.ok(
                accountMapper.map(
                        accountManagementFacade.patchAccountById(accountId, accountMapper.map(patchAccountRequestDto))));
    }

    @Override
    public ResponseEntity<Void> deleteAccountById(Long accountId) {
        accountManagementFacade.deleteAccountById(accountId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateAccountStatusById(Long accountId, UpdateAccountStatusRequestDto updateAccountStatusRequestDto) {
        accountManagementFacade.updateAccountStatusById(accountId, accountMapper.map(updateAccountStatusRequestDto));
        return ResponseEntity.noContent().build();
    }
}
