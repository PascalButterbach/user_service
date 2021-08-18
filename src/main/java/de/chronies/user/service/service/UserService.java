package de.chronies.user.service.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.TokenResponseDto;
import de.chronies.user.service.exceptions.ApiRequestException;
import de.chronies.user.service.models.User;
import de.chronies.user.service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;


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
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;


    public TokenResponseDto signIn(CredentialsDto credentialsDto) {
        var user = userRepository.findByUserEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new ApiRequestException("Email/User not found", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword())) {
            return tokenService.createTokenResponseDto(user);
        }

        throw new ApiRequestException("Invalid password", HttpStatus.BAD_REQUEST);
    }






}
