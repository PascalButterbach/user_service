package de.chronies.user.service.config.interceptors;

import lombok.Data;

@Data
public class BearerTokenWrapper {
    private String token;
}
