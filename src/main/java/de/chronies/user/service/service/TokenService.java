package de.chronies.user.service.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import de.chronies.user.service.dto.responses.TokenResponseDto;
import de.chronies.user.service.exceptions.ApiResponseBase;
import de.chronies.user.service.models.RefreshToken;
import de.chronies.user.service.models.User;
import de.chronies.user.service.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final Algorithm algorithm;

    @Value("${jwt.access_token_duration}")
    private Long ACCESS_TOKEN_MAX_DURATION;

    @Value("${jwt.refresh_token_duration}")
    private Long REFRESH_TOKEN_MAX_DURATION;

    @Value("${jwt.issuer}")
    private String ISSUER;

    public TokenResponseDto createTokenResponseDto(User user) {
        Date now = new Date();

        // TODO: add url to revoke token + rebuild to sent via authheader instead of post ["token"]
        return TokenResponseDto.builder()
                .access_token(createAccessToken(user, now))
                .at_expires_in(ACCESS_TOKEN_MAX_DURATION / 1000)
                .at_expires_at(new Date(now.getTime() + ACCESS_TOKEN_MAX_DURATION))
                .refresh_token(createRefreshToken(user, now))
                .rt_expires_in(REFRESH_TOKEN_MAX_DURATION / 1000)
                .rt_expires_at(new Date(now.getTime() + REFRESH_TOKEN_MAX_DURATION))
                .token_type("bearer")
                .build();
    }

    private String createAccessToken(User user, Date now) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getUser_name())
                .withAudience(String.valueOf(user.getUser_id()), user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + ACCESS_TOKEN_MAX_DURATION))
                .withArrayClaim("scope", new String[0])
                .sign(algorithm);
    }

    private String createRefreshToken(User user, Date now) {
        revokeRefreshToken(user.getUser_id());

        String refresh_token = JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getUser_name())
                .withAudience(String.valueOf(user.getUser_id()), user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + REFRESH_TOKEN_MAX_DURATION))
                .sign(algorithm);

        tokenRepository.create(RefreshToken.builder()
                .user_id(user.getUser_id())
                .token(refresh_token)
                .created(now)
                .expired(new Date(now.getTime() + REFRESH_TOKEN_MAX_DURATION))
                .build());

        return refresh_token;
    }

    public void revokeRefreshToken(int userId) {
        tokenRepository.findActiveTokenByUserId(userId).forEach(refreshToken -> {
            refreshToken.setRevoked(true);
            refreshToken.setExpired(new Date());
            tokenRepository.update(refreshToken);
        });
    }

    public boolean revokeRefreshTokenByRefreshToken(String token){
        RefreshToken refreshToken = getRefreshTokenByRefreshToken(token);

        refreshToken.setRevoked(true);

        return tokenRepository.update(refreshToken);
    }

    public RefreshToken getRefreshTokenByRefreshToken(String token) {
        return tokenRepository.findRefreshTokenByRefreshToken(token)
                .orElseThrow(() -> new ApiResponseBase("Refresh Token not found.", HttpStatus.BAD_REQUEST));
    }
}
