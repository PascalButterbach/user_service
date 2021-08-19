package de.chronies.user.service.controller;

import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.TokenResponseDto;
import de.chronies.user.service.exceptions.ApiExceptionBase;
import de.chronies.user.service.exceptions.ApiValidationExceptionBase;
import de.chronies.user.service.models.User;
import de.chronies.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /*
        @PutMapping("/{id}")
        public boolean updateUser(@RequestBody User user, @PathVariable Long id) {
            return userRepository.update(user, id);
        }

        @DeleteMapping("/{id}")
        public boolean removeUser(@PathVariable Long id) {
            return userRepository.delete(id);
        }


        @PostMapping("/add")
        public boolean registerUser(@RequestBody User user) {
            return userRepository.create(user);
        }
    */

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            var errors = bindingResult.getAllErrors()
                    .stream()
                    .collect(Collectors.toMap(DefaultMessageSourceResolvable::getDefaultMessage,
                            t -> ((FieldError) t).getField()));

            throw new ApiValidationExceptionBase(errors, HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(userService.registerUser(user));
    }


    @PostMapping("/signIn")
    public ResponseEntity<TokenResponseDto> signIn(@RequestBody CredentialsDto credentialsDto) throws ApiExceptionBase {
        return ResponseEntity.ok(userService.signIn(credentialsDto));
    }

}
