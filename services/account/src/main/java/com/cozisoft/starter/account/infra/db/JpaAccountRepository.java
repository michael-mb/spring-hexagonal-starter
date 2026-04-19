package com.cozisoft.starter.account.infra.db;

import com.cozisoft.starter.account.core.model.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaAccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByIdpUserId(@Param("idpUserId") String userId);
}
