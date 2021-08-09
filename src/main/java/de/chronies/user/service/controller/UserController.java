package de.chronies.user.service.controller;

import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.UserDto;
import de.chronies.user.service.models.User;
import de.chronies.user.service.repositories.UserRepository;
import de.chronies.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;


/*
    @GetMapping({"", "/"})
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) throws DataAccessException {
        return userRepository.get(id);
    }

    @PostMapping("/add")
    public boolean createUser(@RequestBody User user) {
        return userRepository.create(user);
    }

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
    public ResponseEntity<UserDto> signIn(@RequestBody CredentialsDto credentialsDto) {
        System.out.println("Trying to login: " + credentialsDto.getLogin());
        return ResponseEntity.ok(userService.signIn(credentialsDto));
    }

    @PostMapping("/validateToken")
    public ResponseEntity<UserDto> signIn(@RequestParam String token) {
        System.out.println("Trying to validate token: " + token);
        return ResponseEntity.ok(userService.validateToken(token));
    }



    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<User> handleException() {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
