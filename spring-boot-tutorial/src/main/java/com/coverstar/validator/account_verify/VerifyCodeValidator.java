package com.coverstar.validator.account_verify;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.coverstar.dao.account.AccountDao;
import com.coverstar.dao.verify_account.VerifyAccountDao;
import com.coverstar.model.Account;
import com.coverstar.model.VerifyAccount;

public class VerifyCodeValidator implements ConstraintValidator<ValidVerifyCode, String>{

	@Autowired
	private VerifyAccountDao verifyAccountDao;

	@Override
	public void initialize(ValidVerifyCode validVerifyCode) {

	}

	@Override
	public boolean isValid(String token, ConstraintValidatorContext context) {
		
		if(token.isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Code not be empty")
					.addConstraintViolation();
			return false;
		} else if(!verifyAccountDao.findByToken(token).isPresent()) {
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Code verification not found")
					.addConstraintViolation();
			return false;
			
			
		} else {
			VerifyAccount verifyAccount = verifyAccountDao.findByToken(token).get();
			if(verifyAccount.isExpired()) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("Verification code has expired")
						.addConstraintViolation();
				return false;
			}
		}
		
		
		return true;
	}
}
