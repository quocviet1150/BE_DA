package com.coverstar.validator.account;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.coverstar.service.AccountService;

public class CreateUsernameValidator implements ConstraintValidator<ValidCreateUsername, String>{

	@Autowired
	private AccountService accountService;
	
	@Override
	public void initialize(ValidCreateUsername constraintAnnotation) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		
		if(accountService.findByUsername(username).isPresent()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Username already taken")
					.addConstraintViolation();
			return false;
		}
		
		return true;
	}

}
