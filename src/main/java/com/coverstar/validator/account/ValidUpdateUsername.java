package com.coverstar.validator.account;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UpdateUsernameValidator.class)
public @interface ValidUpdateUsername {

	String message() default "Invalid username.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
