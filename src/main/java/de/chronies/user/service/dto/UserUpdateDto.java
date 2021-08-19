package de.chronies.user.service.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserUpdateDto {

    @NotEmpty(message = "Email should not be empty.")
    private String email;

    @NotEmpty(message = "Password should not be empty.")
    private String password;

    private String new_email;
    private String new_password;
    private String new_password_repeated;
    private String new_username;

}
