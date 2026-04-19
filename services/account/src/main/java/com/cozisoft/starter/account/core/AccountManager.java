package com.cozisoft.starter.account.core;

import com.cozisoft.starter.account.core.model.entities.Account;
import com.cozisoft.starter.account.core.model.entities.AccountAddress;
import com.cozisoft.starter.account.core.model.entities.AccountStatus;
import com.cozisoft.starter.account.core.model.payload.ExternalUserAccountInfo;
import com.cozisoft.starter.account.core.model.payload.PatchAccountRequest;
import com.cozisoft.starter.account.core.port.in.AccountManagement;
import com.cozisoft.starter.account.core.port.out.AccountRepository;
import com.cozisoft.starter.account.core.port.out.IdPUserManagementAdapter;
import com.cozisoft.starter.shared.core.CustomSecurityContextHolder;
import com.cozisoft.starter.shared.core.errorhandling.ErrorCodes;
import com.cozisoft.starter.shared.core.errorhandling.exceptions.DataAlreadyExistsException;
import com.cozisoft.starter.shared.core.errorhandling.exceptions.DataDoesNotExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AccountManager implements AccountManagement {
    private final AccountRepository accountRepository;
    private final IdPUserManagementAdapter idpUserManagementAdapter;

    @Override
    public Account whoami() {
        String idpUserId = CustomSecurityContextHolder.getAuthenticatedUsername().get();
        Optional<Account> account = accountRepository.findByIdpUserId(idpUserId);
        if (account.isEmpty()) {
            throw new DataDoesNotExistException(ErrorCodes.ACCOUNT_NOT_FOUND, "No account found for user with idpUserId: {}", idpUserId);
        }
        return account.get();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Account createAccount(String idpUserId) {
        Optional<Account> account = accountRepository.findByIdpUserId(idpUserId);
        if (account.isPresent()) {
            throw new DataAlreadyExistsException(ErrorCodes.ACCOUNT_ALREADY_EXISTS, "Account already exists for user with idpUserId: {}", idpUserId);
        }

        Account newAccount = Account.builder()
                .idpUserId(idpUserId)
                .status(AccountStatus.ACTIVE)
                .build();

        Account accountToSave = this.idpUserManagementAdapter.getUserById(idpUserId)
                .map(idpUser -> this.updateAccountDetails(newAccount, idpUser))
                .orElse(newAccount);

        return this.accountRepository.save(accountToSave);
    }

    private Account updateAccountDetails(Account newAccount, ExternalUserAccountInfo idpUser) {
        return newAccount.toBuilder()
                .firstName(idpUser.getGivenName())
                .lastName(idpUser.getFamilyName())
                .email(idpUser.getEmail())
                .build();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Account patchAccountById(Long accountId, PatchAccountRequest request) {
        Account toPatch = getAccountById(accountId).orElseThrow(() ->
                new DataDoesNotExistException(ErrorCodes.ACCOUNT_NOT_FOUND, "No account found for user with id: {}", String.valueOf(accountId)));

        toPatch.setFirstName(request.getFirstName());
        toPatch.setLastName(request.getLastName());
        toPatch.setPhoneNumber(request.getPhoneNumber());
        toPatch.setEmail(request.getEmail());
        toPatch.setGender(request.getGender());

        if (request.getEmail() != null || request.getFirstName() != null || request.getLastName() != null) {
            ExternalUserAccountInfo idpPatch = ExternalUserAccountInfo.builder()
                    .email(request.getEmail())
                    .givenName(request.getFirstName())
                    .familyName(request.getLastName())
                    .build();
            idpUserManagementAdapter.patchUser(idpPatch);
        }

        if (request.getAddress() != null) {
            AccountAddress accountAddress = AccountAddress.builder()
                    .country(request.getAddress().getCountry())
                    .state(request.getAddress().getState())
                    .city(request.getAddress().getCity())
                    .street(request.getAddress().getStreet())
                    .zipCode(request.getAddress().getZipCode())
                    .build();
            toPatch.setAddress(accountAddress);
        }

        return accountRepository.save(toPatch);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteAccountById(Long accountId) {
        accountRepository.deleteById(accountId, true);
    }

    @Override
    public Account getAccountByIdpUserId(String idpUserId) {
        Optional<Account> account = accountRepository.findByIdpUserId(idpUserId);
        if (account.isEmpty()) {
            throw new DataDoesNotExistException(ErrorCodes.ACCOUNT_NOT_FOUND, "No account found for user with idpUserId: {}", idpUserId);
        }
        return account.get();
    }

    @Override
    public Optional<Account> getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void suspendAccount(Long accountId) {
        updateAccountStatus(accountId, AccountStatus.SUSPENDED);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void activateAccount(Long accountId) {
        updateAccountStatus(accountId, AccountStatus.ACTIVE);
    }

    private void updateAccountStatus(Long accountId, AccountStatus status) {
        Account account = getAccountById(accountId).orElseThrow(() ->
                new DataDoesNotExistException(ErrorCodes.ACCOUNT_NOT_FOUND, "No account found with id: {}", String.valueOf(accountId))
        );
        if (account.getStatus().equals(status)) {
            return;
        }
        account.setStatus(status);
    }
}
