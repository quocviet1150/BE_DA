package com.coverstar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreateDto {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String repeatPassword;

    @Override
    public String toString() {
        return "AccountCreateDto [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
                + ", repeatPassword=" + repeatPassword + "]";
    }

}
