package de.chronies.user.service.controller;

import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.UserUpdateDto;
import de.chronies.user.service.dto.responses.ApiResponseDto;
import de.chronies.user.service.dto.responses.TokenResponseDto;
import de.chronies.user.service.exceptions.ApiException;
import de.chronies.user.service.exceptions.ApiValidationException;
import de.chronies.user.service.models.User;
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

    @PostMapping("/signIn")
    public ResponseEntity<TokenResponseDto> signIn(@RequestBody CredentialsDto credentialsDto) throws ApiException {
        return ResponseEntity.ok(authService.signIn(credentialsDto));
    }

    @PostMapping("/signUp")
    public ResponseEntity<TokenResponseDto> signUp(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            var errors = bindingResult.getAllErrors()
                    .stream()
                    .collect(Collectors.toMap(DefaultMessageSourceResolvable::getDefaultMessage,
                            t -> ((FieldError) t).getField()));

            throw new ApiValidationException(errors, HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PutMapping({"/update"})
    public ResponseEntity<ApiResponseDto> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            var errors = bindingResult.getAllErrors()
                    .stream()
                    .collect(Collectors.toMap(DefaultMessageSourceResolvable::getDefaultMessage,
                            t -> ((FieldError) t).getField()));

            throw new ApiValidationException(errors, HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(userService.update(userUpdateDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> removeUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.delete(id));
    }

}
