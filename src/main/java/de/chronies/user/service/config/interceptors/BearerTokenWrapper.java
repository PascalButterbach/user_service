package de.chronies.user.service.config.interceptors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class BearerTokenWrapper {
    private String token;
}
