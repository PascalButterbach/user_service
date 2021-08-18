package de.chronies.user.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TokenResponseDto {

    private String token_type;
    private String access_token;
    private Long expires_in;
    private String refresh_token;

}
