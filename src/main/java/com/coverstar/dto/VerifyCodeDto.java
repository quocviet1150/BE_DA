package com.coverstar.dto;

import com.coverstar.validator.account_verify.ValidVerifyCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyCodeDto {

	@ValidVerifyCode
	private String token;

}
