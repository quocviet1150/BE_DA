package com.coverstar.validator.account;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.coverstar.dto.AccountCreateDto;
import com.coverstar.dto.AccountUpdateDto;

public class RepeatPasswordValidator implements ConstraintValidator<ValidRepeatPassword, Object>{

	@Override
	public void initialize(ValidRepeatPassword constraintAnnotation) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		
		if(value instanceof AccountCreateDto) {
			AccountCreateDto dto = (AccountCreateDto) value;
			
			if (dto.getPassword().isEmpty()) {
				context.buildConstraintViolationWithTemplate( "Password not be empty" )
		         .addPropertyNode( "repeatPassword" )
		         .addConstraintViolation();
				return false;
				
			} 
			
			if (!dto.getPassword().equals(dto.getRepeatPassword())) {
				context.buildConstraintViolationWithTemplate( "Passwords don't match" )
		         .addPropertyNode( "repeatPassword" )
		         .addConstraintViolation();
				return false;
				
			} 
		}
		
		if(value instanceof AccountUpdateDto) {
			AccountUpdateDto dto = (AccountUpdateDto) value;
			
			if (dto.getPassword().isEmpty()) {
				context.buildConstraintViolationWithTemplate( "Password not be empty" )
		         .addPropertyNode( "repeatPassword" )
		         .addConstraintViolation();
				return false;
				
			} 
			
			if (!dto.getPassword().equals(dto.getRepeatPassword())) {
				context.buildConstraintViolationWithTemplate( "Passwords don't match" )
		         .addPropertyNode( "repeatPassword" )
		         .addConstraintViolation();
				return false;
				
			} 
		}
		 
		return true;
	}

}
