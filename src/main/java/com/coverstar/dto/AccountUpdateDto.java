package com.coverstar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUpdateDto {

	private Long id;
	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private Integer sex;
	private String phoneNumber;
}
