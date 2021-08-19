package de.chronies.user.service.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RefreshToken {

    private int token_id;
    private int user_id;

    private String token;
    private Date created;
    private Date expired;

    private boolean revoked;

}
