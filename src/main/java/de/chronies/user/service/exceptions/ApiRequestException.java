package de.chronies.user.service.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiRequestException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

}
