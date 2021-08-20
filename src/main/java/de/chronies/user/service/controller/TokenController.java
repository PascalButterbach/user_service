package de.chronies.user.service.controller;

import de.chronies.user.service.config.interceptors.BearerTokenWrapper;
import de.chronies.user.service.dto.responses.ApiResponseDto;
import de.chronies.user.service.dto.responses.GatewayAuthResponseDto;
import de.chronies.user.service.dto.responses.TokenResponseDto;
import de.chronies.user.service.service.AuthService;
import de.chronies.user.service.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {

    private final AuthService authService;
    private final TokenService tokenService;
    private final BearerTokenWrapper bearerTokenWrapper;

    @PostMapping("/validateToken")
    public ResponseEntity<GatewayAuthResponseDto> validateToken() {
        return ResponseEntity.ok(authService.validateToken(bearerTokenWrapper.getToken()));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenResponseDto> refreshToken() {
        return ResponseEntity.ok(authService.refreshToken(bearerTokenWrapper.getToken()));
    }

    @PostMapping("/revokeRefreshToken")
    public ResponseEntity<ApiResponseDto> revokeRefreshToken(HttpServletRequest request) {

        boolean tokenIsRevoked = tokenService.revokeRefreshTokenByRefreshToken(bearerTokenWrapper.getToken());

        String message = (tokenIsRevoked) ? "Token successfully revoked." : "Token was not revoked. Contact support.";

        return ResponseEntity.ok(ApiResponseDto.builder()
                .message(message)
                .path(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString())
                .status(HttpStatus.OK)
                .time(Instant.now()).build());
    }
}
