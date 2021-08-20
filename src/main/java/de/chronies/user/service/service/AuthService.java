package de.chronies.user.service.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.GatewayAuthResponseDto;
import de.chronies.user.service.dto.TokenResponseDto;
import de.chronies.user.service.exceptions.ApiResponseBase;
import de.chronies.user.service.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new ApiResponseBase("Account is disabled.", HttpStatus.BAD_REQUEST);
        }

        throw new ApiResponseBase("Invalid password.", HttpStatus.BAD_REQUEST);
    }


    public TokenResponseDto refreshToken(String token) {
        String user_email = validateToken(token).getUser_email();

        var user = userService.findUserByEmail(user_email);

        return  tokenService.createTokenResponseDto(user);
    }


    public GatewayAuthResponseDto validateToken(String token) {
        String email;

        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            email = decodedJWT.getAudience().get(1);
        } catch (JWTVerificationException e) {
            throw new ApiResponseBase(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        User user = userService.findUserByEmail(email);

        return GatewayAuthResponseDto.builder()
                .user_id(user.getUser_id())
                .user_email(user.getEmail())
                .build();
    }
}
