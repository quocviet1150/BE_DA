package com.coverstar.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.coverstar.dto.AccountCreateDto;
import com.coverstar.dto.LoginDto;
import com.coverstar.dto.VerifyCodeDto;
import com.coverstar.entity.Account;

public interface AccountService {

	Account createMember(AccountCreateDto accountDto) throws Exception;
	
	Account createAdmin(AccountCreateDto accountDto);
	
	Optional<Account> findByEmail(String email);

	Optional<Account> findByUsername(String username);
	
	Optional<Account> findById(Long id);
	
	void verifyCode(VerifyCodeDto verifyCodeDto);

    void changePassword(String username, String newPassword);

	boolean checkPassword(String username, String oldPassword);

	void forgotPassword(String usernameOrEmail);

	Map<String, String> authenticateUser(LoginDto loginDto);

	void unlockAccount(String usernameOrEmail);

	List<Account> getAllAccount();
}
