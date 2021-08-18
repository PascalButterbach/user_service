package de.chronies.user.service.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.UserDto;
import de.chronies.user.service.exceptions.ApiRequestException;
import de.chronies.user.service.mapper.UserMapper;
import de.chronies.user.service.models.User;
import de.chronies.user.service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Algorithm algorithm;

    @Value("${jwt.duration}")
    private Long MAX_DURATION;

    public UserDto signIn(CredentialsDto credentialsDto) {
        var user = userRepository.findByUserName(credentialsDto.getLogin())
                .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword())) {
            return userMapper.toUserDto(user, createToken(user));
        }

        throw new ApiRequestException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto validateToken(String token) {
        String username = "";

        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            username = decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            throw new ApiRequestException(e.getMessage(), HttpStatus.OK);
        }

        Optional<User> userOptional = userRepository.findByUserName(username);

        if (userOptional.isEmpty()) {
            throw new ApiRequestException("User not found", HttpStatus.OK);
        }

        User user = userOptional.get();
        return userMapper.toUserDto(user, createToken(user));
    }

    private String createToken(User user) {
        Date now = new Date();

        return JWT.create()
                .withSubject(user.getUser_name())
                .withIssuedAt(now)
                .withExpiresAt(new Date(System.currentTimeMillis() + MAX_DURATION))
                .sign(algorithm);
    }
}
