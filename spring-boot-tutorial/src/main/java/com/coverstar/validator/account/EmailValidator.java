package com.coverstar.validator.account;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.coverstar.constant.RegexUtil;

public class EmailValidator implements ConstraintValidator<ValidEmail, String>{

	
	@Override
	public void initialize(ValidEmail constraintAnnotation) {
    }
	
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		
		if(email.isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Email not be empty")
					.addConstraintViolation();
			
			return false;
		}
		if(!RegexUtil.validateEmail(email)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Email invalid")
					.addConstraintViolation();
			
			return false;
		}
		
		return true;
	}
	

}
