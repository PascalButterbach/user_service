package de.chronies.user.service.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TokenResponseDto {

    private String token_type;
    private String access_token;
    private Long at_expires_in;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE dd MMM yyyy HH:mm:ss z", timezone = "Europe/Berlin")
    private Date at_expires_at;
    private String refresh_token;
    private Long rt_expires_in;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE dd MMM yyyy HH:mm:ss z", timezone = "Europe/Berlin")
    private Date rt_expires_at;

}
