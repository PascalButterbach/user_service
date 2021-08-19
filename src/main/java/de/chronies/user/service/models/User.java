package de.chronies.user.service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private @Id int user_id;

    //todo : min/max length
    @NotEmpty(message = "Username should not be empty.")
    private String user_name;

    //todo: email patternmatcher
    @NotEmpty(message = "Email should not be empty.")
    private String email;

    //todo: complexity rules
    @NotEmpty(message = "Password should not be empty.")
    private String password;

    private LocalDateTime created;
    private LocalDateTime changed;

    private boolean active;
}
