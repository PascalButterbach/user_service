package de.chronies.user.service.controller;

import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.TokenResponseDto;
import de.chronies.user.service.dto.UserUpdateDto;
import de.chronies.user.service.exceptions.ApiResponseBase;
import de.chronies.user.service.exceptions.ApiValidationResponseBase;
import de.chronies.user.service.models.User;
import de.chronies.user.service.responses.ApiResponse;
import de.chronies.user.service.service.AuthService;
import de.chronies.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    /*
        @PutMapping("/{id}")
        public boolean updateUser(@RequestBody User user, @PathVariable Long id) {
            return userRepository.update(user, id);
        }

        @DeleteMapping("/{id}")
        public boolean removeUser(@PathVariable Long id) {
            return userRepository.delete(id);
        }
    */

    @PostMapping("/signIn")
    public ResponseEntity<TokenResponseDto> signIn(@RequestBody CredentialsDto credentialsDto) throws ApiResponseBase {
        return ResponseEntity.ok(authService.signIn(credentialsDto));
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            var errors = bindingResult.getAllErrors()
                    .stream()
                    .collect(Collectors.toMap(DefaultMessageSourceResolvable::getDefaultMessage,
                            t -> ((FieldError) t).getField()));

            throw new ApiValidationResponseBase(errors, HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PutMapping({"/update"})
    public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            var errors = bindingResult.getAllErrors()
                    .stream()
                    .collect(Collectors.toMap(DefaultMessageSourceResolvable::getDefaultMessage,
                            t -> ((FieldError) t).getField()));

            throw new ApiValidationResponseBase(errors, HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(userService.update(userUpdateDto));
    }

}
