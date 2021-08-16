package de.chronies.user.service.service;

import de.chronies.user.service.dto.CredentialsDto;
import de.chronies.user.service.dto.UserDto;
import de.chronies.user.service.exceptions.ApiRequestException;
import de.chronies.user.service.mapper.UserMapper;
import de.chronies.user.service.models.User;
import de.chronies.user.service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.duration}")
    private Long MAX_DURATION;


    public UserDto signIn(CredentialsDto credentialsDto) {
        var user = userRepository.findByUserName(credentialsDto.getLogin())
                .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword())) {
            return userMapper.toUserDto(user, createToken(user));
        }

        throw new ApiRequestException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto validateToken(String token) {

        System.out.println("--------------------------");
        String login = "";
        System.out.println("token: " + token);
        try {
            login = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new ApiRequestException("Weird JWT Token.", HttpStatus.BAD_REQUEST);
        }
        System.out.println("token: " + token);
        
        System.out.println("Before repo");
        Optional<User> userOptional = userRepository.findByUserName(login);
        System.out.println("After repo - Optional: " + userOptional.isEmpty());
        System.out.println("--------------------------");

        if (userOptional.isEmpty()) {
            throw new ApiRequestException("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        return userMapper.toUserDto(user, createToken(user));
    }

    private String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUser_name());

        Date now = new Date();
        Date validity = new Date(now.getTime() + MAX_DURATION);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
