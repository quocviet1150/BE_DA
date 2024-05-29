package com.coverstar.validator.account_verify;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VerifyCodeValidator.class)
@Documented
public @interface ValidVerifyCode {

	String message() default "Invalid code.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
