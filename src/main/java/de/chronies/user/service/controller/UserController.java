package de.chronies.user.service.controller;

import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.UserDto;
import de.chronies.user.service.exceptions.ApiRequestException;
import de.chronies.user.service.models.User;
import de.chronies.user.service.repositories.UserRepository;
import de.chronies.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
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
*/

    @PostMapping("/add")
    public boolean registerUser(@RequestBody User user) {
        return userRepository.create(user);
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserDto> signIn(@RequestBody CredentialsDto credentialsDto) throws ApiRequestException {
        return ResponseEntity.ok(userService.signIn(credentialsDto));
    }

    @PostMapping("/validateToken")
    public ResponseEntity<UserDto> validateToken(@RequestParam String token) {
        return ResponseEntity.ok(userService.validateToken(token));
    }

}
