package de.chronies.user.service.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ApiValidationExceptionBase extends RuntimeException  {

    private final Map<String, String> messages;
    private final HttpStatus status;

}
