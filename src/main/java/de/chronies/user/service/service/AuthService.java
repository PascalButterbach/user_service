package de.chronies.user.service.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.responses.GatewayAuthResponseDto;
import de.chronies.user.service.dto.responses.TokenResponseDto;
import de.chronies.user.service.exceptions.ApiException;
import de.chronies.user.service.model.RefreshToken;
import de.chronies.user.service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final Algorithm algorithm;
    private final PasswordEncoder passwordEncoder;

    public TokenResponseDto signIn(CredentialsDto credentialsDto) {
        var user = userService.findUserByEmail(credentialsDto.getEmail());

        boolean passwordCorrect = passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword());
        if (passwordCorrect) {
            return tokenService.createTokenResponseDto(user);
        }

        if (!user.isActive()) {
            throw new ApiException("Account is disabled.", HttpStatus.BAD_REQUEST);
        }

        throw new ApiException("Invalid password.", HttpStatus.BAD_REQUEST);
    }


    public TokenResponseDto refreshToken(String token) {
        GatewayAuthResponseDto dto = validateToken(token);

        RefreshToken refreshToken = tokenService.getRefreshTokenByRefreshToken(token);
        if(refreshToken.isRevoked() || refreshToken.getExpired().before(new Date(System.currentTimeMillis()))){
            throw new ApiException("Refresh Token is expired/revoked.", HttpStatus.BAD_REQUEST);
        }

        User user = userService.findUserByEmail(dto.getUser_email());

        return  tokenService.createTokenResponseDto(user);
    }


    public GatewayAuthResponseDto validateToken(String token) {
        String email;

        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            email = decodedJWT.getAudience().get(1);
        } catch (JWTVerificationException e) {
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        User user = userService.findUserByEmail(email);

        return GatewayAuthResponseDto.builder()
                .user_id(user.getUser_id())
                .user_email(user.getEmail())
                .build();
    }
}
