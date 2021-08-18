package de.chronies.user.service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private @Id int user_id;

    private String user_name;

    private String email;

    private String password;

    private LocalDateTime created;
    private LocalDateTime changed;

    private boolean active;
}
