package com.coverstar.validator.account;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.coverstar.constant.RegexUtil;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String>{

	@Override
	public void initialize(ValidUsername constraintAnnotation) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		
		if(username.isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Username not be empty")
					.addConstraintViolation();
			return false;
		} else if(!RegexUtil.validUsername(username)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Username invalid")
					.addConstraintViolation();
			return false;
		}
		
		return true;
	}

}
