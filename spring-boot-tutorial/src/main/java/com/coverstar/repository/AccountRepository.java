package com.coverstar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coverstar.model.Account;
import org.springframework.transaction.annotation.Transactional;

import java.lang.String;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByUsernameOrEmail(String username, String email);

	Optional<Account> findByEmail(String email);

	Optional<Account> findByUsername(String username);

	@Transactional
	@Query("SELECT a FROM Account a WHERE a.email = :email")
	Account getAccountByEmail(@Param("email") String email);
}