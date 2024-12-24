package com.coverstar.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AccountDto {

	private Long id;
	@NotBlank(message = "Username is required")
	@Length(min = 5, max = 50, message = "Username name must be between 5-100 characters")
	private String username;

	@Length(min = 5, max = 80, message = "email name must be between 10-100 characters")
	@Email(message = "email is invalid")
	private String email;

	@NotBlank(message = "firstName is required")
	@Length(min = 5, max = 255, message = "firstName name must be between 5-255 characters")
	private String firstName;

	@NotBlank(message = "lastName is required")
	@Length(min = 5, max = 255, message = "lastName name must be between 5-255 characters")
	private String lastName;
	private String dateOfBirth;
	private Integer sex;
	private String phoneNumber;
	private String directoryPath;
}
