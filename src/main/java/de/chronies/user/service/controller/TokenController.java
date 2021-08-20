package de.chronies.user.service.controller;

import de.chronies.user.service.config.interceptors.BearerTokenWrapper;
import de.chronies.user.service.responses.GatewayAuthResponseDto;
import de.chronies.user.service.responses.TokenResponseDto;
import de.chronies.user.service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {

    private final AuthService authService;
    private final BearerTokenWrapper bearerTokenWrapper;

    @PostMapping("/validateToken")
    public ResponseEntity<GatewayAuthResponseDto> validateToken() {
        return ResponseEntity.ok(authService.validateToken(bearerTokenWrapper.getToken()));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenResponseDto> refreshToken() {
        return ResponseEntity.ok(authService.refreshToken(bearerTokenWrapper.getToken()));
    }

}
