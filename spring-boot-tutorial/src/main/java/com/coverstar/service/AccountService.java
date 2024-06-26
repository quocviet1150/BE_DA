package com.coverstar.service;

import java.util.Optional;

import com.coverstar.dto.AccountCreateDto;
import com.coverstar.dto.VerifyCodeDto;
import com.coverstar.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AccountService {

	public Account createMember(AccountCreateDto accountDto) throws Exception;
	
	public Account createAdmin(AccountCreateDto accountDto);
	
	Optional<Account> findByUsernameOrEmail(String username, String email);

	Optional<Account> findByEmail(String email);

	Optional<Account> findByUsername(String username);
	
	Optional<Account> findById(Long id);
	
	public void verifyCode(VerifyCodeDto verifyCodeDto);

}
