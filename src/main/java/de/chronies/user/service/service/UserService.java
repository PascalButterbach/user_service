package de.chronies.user.service.service;

import de.chronies.user.service.responses.TokenResponseDto;
import de.chronies.user.service.dto.UserUpdateDto;
import de.chronies.user.service.exceptions.ApiResponseBase;
import de.chronies.user.service.models.User;
import de.chronies.user.service.repositories.UserRepository;
import de.chronies.user.service.responses.ApiResponseDto;
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


    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiResponseBase("Email/User not found.", HttpStatus.NOT_FOUND));
    }


    public TokenResponseDto registerUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.create(user);

        user = findUserByEmail(user.getEmail());

        return tokenService.createTokenResponseDto(user);
    }

    public ApiResponseDto update(UserUpdateDto userUpdateDto) {
        // fetch user
        var user = findUserByEmail(userUpdateDto.getEmail());

        // validate password
        boolean passwordIncorrect = !passwordEncoder.matches(userUpdateDto.getPassword(), user.getPassword());
        if (passwordIncorrect)
            throw new ApiResponseBase("Invalid password.", HttpStatus.BAD_REQUEST);

        // update password if required
        boolean newPasswordNotNull = userUpdateDto.getNew_password() != null;
        boolean newPasswordRepeatedNotNull = userUpdateDto.getNew_password_repeated() != null;
        if (newPasswordNotNull && !newPasswordRepeatedNotNull || !newPasswordNotNull && newPasswordRepeatedNotNull)
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
        if (updateEmail)
            user.setEmail(userUpdateDto.getNew_email());

        //update username
        boolean updateUserName = userUpdateDto.getNew_username() != null;
        if (updateUserName)
            user.setUser_name(userUpdateDto.getNew_username());

        //persist updates
        userRepository.update(user);

        // revoke token -> force relog
        boolean somethingChanged = newPasswordNotNull || updateEmail || updateUserName;
        if(somethingChanged)
            tokenService.revokeRefreshToken(user.getUser_id());

        String message = somethingChanged ? "Your account has been updated. Please login again" : "No changes were made.";

        return ApiResponseDto.builder()
                .message(message)
                .status(HttpStatus.OK)
                .time(Instant.now())
                .path("/user/update").build();
    }

}
