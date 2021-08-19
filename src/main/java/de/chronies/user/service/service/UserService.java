package de.chronies.user.service.service;

import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.TokenResponseDto;
import de.chronies.user.service.dto.UserUpdateDto;
import de.chronies.user.service.exceptions.ApiResponseBase;
import de.chronies.user.service.models.User;
import de.chronies.user.service.repositories.UserRepository;
import de.chronies.user.service.responses.ApiResponse;
import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;


    public TokenResponseDto signIn(CredentialsDto credentialsDto) {
        var user = userRepository.findByUserEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new ApiResponseBase("Email/User not found.", HttpStatus.NOT_FOUND));

        boolean passwordCorrect = passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword());
        if (passwordCorrect) {
            return tokenService.createTokenResponseDto(user);
        }

        if (!user.isActive()) {
            throw new ApiResponseBase("Account is disabled.", HttpStatus.BAD_REQUEST);
        }

        throw new ApiResponseBase("Invalid password.", HttpStatus.BAD_REQUEST);
    }


    public TokenResponseDto registerUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.create(user);

        return tokenService.createTokenResponseDto(user);
    }

    public ApiResponse update(UserUpdateDto userUpdateDto) {
        // fetch user
        var user = userRepository.findByUserEmail(userUpdateDto.getEmail())
                .orElseThrow(() -> new ApiResponseBase("Email/User not found.", HttpStatus.NOT_FOUND));

        // validate password
        boolean passwordIncorrect = !passwordEncoder.matches(userUpdateDto.getPassword(), user.getPassword());
        if (passwordIncorrect)
            throw new ApiResponseBase("Invalid password.", HttpStatus.BAD_REQUEST);

        // update password if required
        boolean newPasswordNotNull = userUpdateDto.getNew_password() != null;
        boolean newPasswordRepeatedNotNull = userUpdateDto.getNew_password_repeated() != null;
        if(newPasswordNotNull && !newPasswordRepeatedNotNull || !newPasswordNotNull && newPasswordRepeatedNotNull)
            throw new ApiResponseBase("Please verify your new Password.", HttpStatus.BAD_REQUEST);

        if (newPasswordNotNull) {
            boolean updatedPasswordEqual = userUpdateDto.getNew_password().equals(userUpdateDto.getNew_password_repeated());

            if (updatedPasswordEqual)
                user.setPassword(passwordEncoder.encode(userUpdateDto.getNew_password()));

            if (!updatedPasswordEqual)
                throw new ApiResponseBase("New Passwords doesnt match.", HttpStatus.BAD_REQUEST);
        }

        //update email
        boolean updateEmail = userUpdateDto.getNew_email() != null;
        if(updateEmail)
            user.setEmail(userUpdateDto.getNew_email());

        //update username
        boolean updateUserName = userUpdateDto.getNew_username() != null;
        if(updateUserName)
            user.setUserName(userUpdateDto.getNew_username());

        userRepository.update(user);

        // Todo : revoke refresh token if email has changed

        String message = newPasswordNotNull ||updateEmail || updateUserName ?
                "Your account has been updated. Please login again" : "No changes were made.";

        return ApiResponse.builder()
                .message(message)
                .status(HttpStatus.OK)
                .time(Instant.now())
                .path("/user/update").build();
    }

}
