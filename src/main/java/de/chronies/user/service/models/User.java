package de.chronies.user.service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private @Id int user_id;

    @NotNull(message = "Username darf nicht null sein.")
    @NotEmpty(message = "Username darf nicht leer sein.")
    private String user_name;

    @NotNull(message = "Email darf nicht null sein.")
    @NotEmpty(message = "Email darf nicht leer sein.")
    private String email;

    @NotNull(message = "Password darf nicht null sein.")
    @NotEmpty(message = "Password darf nicht leer sein.")
    private String password;

    private LocalDateTime created;
    private LocalDateTime changed;

    private boolean active;
}
