package de.chronies.user.service.controller;

import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.GatewayAuthResponseDto;
import de.chronies.user.service.dto.TokenDto;
import de.chronies.user.service.dto.TokenResponseDto;
import de.chronies.user.service.exceptions.ApiRequestException;
import de.chronies.user.service.repositories.UserRepository;
import de.chronies.user.service.service.TokenService;
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
    private final TokenService tokenService;

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

    @PostMapping("/signIn")
    public ResponseEntity<TokenResponseDto> signIn(@RequestBody CredentialsDto credentialsDto) throws ApiRequestException {
        return ResponseEntity.ok(userService.signIn(credentialsDto));
    }

    @PostMapping("/validateToken")
    public ResponseEntity<GatewayAuthResponseDto> validateToken(@RequestBody TokenDto tokenDto) {
        return ResponseEntity.ok(tokenService.validateToken(tokenDto.getToken()));
    }

}
