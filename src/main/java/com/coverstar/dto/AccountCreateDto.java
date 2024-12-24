package com.coverstar.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AccountCreateDto {

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

    @NotBlank(message = "password is required")
    @Length(min = 8, max = 255, message = "lastName name must be between 8-255 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).+$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character")
    private String password;

    @NotBlank(message = "password is required")
    @Length(min = 8, max = 255, message = "lastName name must be between 8-255 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).+$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character")
    private String repeatPassword;

    @Override
    public String toString() {
        return "AccountCreateDto [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
                + ", repeatPassword=" + repeatPassword + "]";
    }

}
