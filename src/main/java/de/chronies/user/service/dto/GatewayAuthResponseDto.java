package de.chronies.user.service.dto;

import lombok.*;

@Data
@Builder
public class GatewayAuthResponseDto{

    private int user_id;
    private String user_email;

}
