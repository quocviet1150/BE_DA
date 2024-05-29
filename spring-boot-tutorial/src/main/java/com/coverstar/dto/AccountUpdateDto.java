package com.coverstar.dto;

import com.coverstar.validator.account.ValidEmail;
import com.coverstar.validator.account.ValidPassword;
import com.coverstar.validator.account.ValidRepeatPassword;
import com.coverstar.validator.account.ValidUpdateEmail;
import com.coverstar.validator.account.ValidUpdateUsername;
import com.coverstar.validator.account.ValidUsername;
import lombok.Getter;
import lombok.Setter;

@ValidRepeatPassword
@ValidUpdateEmail
@ValidUpdateUsername
@Getter
@Setter
public class AccountUpdateDto {

	private Long id;
	
	@ValidUsername
	private String username;
	
	@ValidEmail
	private String email;
	
	@ValidPassword
	private String password;
	private String repeatPassword;

	@Override
	public String toString() {
		return "AccountUpdateDto [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", repeatPassword=" + repeatPassword + "]";
	}

}
