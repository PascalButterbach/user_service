package de.chronies.user.service.dto.responses;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GatewayAuthResponseDto{

    private int user_id;
    private String user_email;

}
