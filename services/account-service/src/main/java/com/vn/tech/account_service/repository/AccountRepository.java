package com.vn.tech.account_service.repository;

import com.vn.tech.account_service.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    boolean existsByUsername(String username);
    AccountEntity findByUsername(String username);
}
