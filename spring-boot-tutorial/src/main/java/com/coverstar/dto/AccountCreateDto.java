package com.coverstar.dto;

import com.coverstar.validator.account.ValidCreateEmail;
import com.coverstar.validator.account.ValidCreateUsername;
import com.coverstar.validator.account.ValidEmail;
import com.coverstar.validator.account.ValidPassword;
import com.coverstar.validator.account.ValidRepeatPassword;
import com.coverstar.validator.account.ValidUsername;
import lombok.Getter;
import lombok.Setter;

@ValidRepeatPassword
@Getter
@Setter
public class AccountCreateDto {

	private Long id;
	
	@ValidUsername
	@ValidCreateUsername
	private String username;

	@ValidEmail
	@ValidCreateEmail
	private String email;

	private String firstName;

	private String lastName;

	@ValidPassword
	private String password;

	private String repeatPassword;

	@Override
	public String toString() {
		return "AccountCreateDto [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", repeatPassword=" + repeatPassword + "]";
	}

}
