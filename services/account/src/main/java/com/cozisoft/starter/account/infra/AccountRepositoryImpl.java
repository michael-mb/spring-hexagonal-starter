package com.cozisoft.starter.account.infra;

import com.cozisoft.starter.account.core.model.entities.Account;
import com.cozisoft.starter.account.core.port.out.AccountRepository;
import com.cozisoft.starter.account.infra.db.JpaAccountRepository;
import com.cozisoft.starter.shared.infra.DefaultCrudRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {
    private final DefaultCrudRepositoryImpl<Account> crudRepository;
    private final JpaAccountRepository repository;

    public AccountRepositoryImpl(JpaAccountRepository repository) {
        this.crudRepository = DefaultCrudRepositoryImpl.of(repository, Account.class);
        this.repository = repository;
    }

    @Override
    public Optional<Account> findByIdpUserId(String idpUserId) {
        return repository.findByIdpUserId(idpUserId);
    }

    @Override
    public Account save(Account entity) {
        return this.crudRepository.save(entity);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return this.crudRepository.findById(id);
    }

    @Override
    public List<Account> findAll() {
        return this.crudRepository.findAll();
    }

    @Override
    public Account deleteById(Long id, boolean hardDelete) {
        return this.crudRepository.deleteById(id, hardDelete);
    }
}
