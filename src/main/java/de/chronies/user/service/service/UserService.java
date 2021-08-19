package de.chronies.user.service.service;

import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.TokenResponseDto;
import de.chronies.user.service.exceptions.ApiExceptionBase;
import de.chronies.user.service.models.User;
import de.chronies.user.service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;


    public TokenResponseDto signIn(CredentialsDto credentialsDto) {
        var user = userRepository.findByUserEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new ApiExceptionBase("Email/User not found", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword())) {
            return tokenService.createTokenResponseDto(user);
        }

        throw new ApiExceptionBase("Invalid password", HttpStatus.BAD_REQUEST);
    }


    public TokenResponseDto registerUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.create(user);

        return tokenService.createTokenResponseDto(user);
    }
}
