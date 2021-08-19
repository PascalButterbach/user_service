package de.chronies.user.service.controller;

import de.chronies.user.service.dto.GatewayAuthResponseDto;
import de.chronies.user.service.dto.TokenDto;
import de.chronies.user.service.service.AuthService;
import de.chronies.user.service.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {
    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/validateToken")
    public ResponseEntity<GatewayAuthResponseDto> validateToken(@RequestBody TokenDto tokenDto) {
        return ResponseEntity.ok(authService.validateToken(tokenDto.getToken()));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<GatewayAuthResponseDto> refreshToken(@RequestBody TokenDto tokenDto) {
        return ResponseEntity.ok(authService.refreshToken(tokenDto.getToken()));
    }

}
