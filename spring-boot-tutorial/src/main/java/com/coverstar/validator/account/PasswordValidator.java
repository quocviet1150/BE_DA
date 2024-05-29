package com.coverstar.validator.account;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.coverstar.constant.RegexUtil;


public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

	
	@Override
	public void initialize(ValidPassword constraintAnnotation) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		Map<String, String> maps = new HashMap<String, String>();
		
		if(!RegexUtil.validatePassword(password, maps)) {
			String errorMessage = maps.get("errorMessage");
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(errorMessage)
					.addConstraintViolation();
			
			return false;
		}
		
		return true;
	}

}
