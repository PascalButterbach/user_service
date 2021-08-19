package de.chronies.user.service.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.chronies.user.service.dto.GatewayAuthResponseDto;
import de.chronies.user.service.dto.TokenResponseDto;
import de.chronies.user.service.exceptions.ApiResponseBase;
import de.chronies.user.service.models.User;
import de.chronies.user.service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;
    private final Algorithm algorithm;

    @Value("${jwt.duration}")
    private Long MAX_DURATION;

    @Value("${jwt.issuer}")
    private String ISSUER;

    public GatewayAuthResponseDto validateToken(String token) {
        String email;

        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            email = decodedJWT.getAudience().get(1);
        } catch (JWTVerificationException e) {
            throw new ApiResponseBase(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOptional = userRepository.findByUserEmail(email);

        if (userOptional.isEmpty()) {
            throw new ApiResponseBase("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        return GatewayAuthResponseDto.builder()
                .user_id(user.getUser_id())
                .build();
    }


    public TokenResponseDto createTokenResponseDto(User user) {
        Date now = new Date();

        return TokenResponseDto.builder()
                .access_token(getAccess_token(user, now))
                .refresh_token(getAccess_token(user, now))
                .expires_in(MAX_DURATION / 1000)
                .token_type("bearer")
                .build();
    }

    private String getAccess_token(User user, Date now) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getUser_name())
                .withAudience(String.valueOf(user.getUser_id()), user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(new Date(System.currentTimeMillis() + MAX_DURATION))
                .withArrayClaim("scope", new String[0])
                .sign(algorithm);
    }

    // TODO: REVOKE REFRESHTOKEN

    // TODO: REMOVE REFRESHTOKEN


}
