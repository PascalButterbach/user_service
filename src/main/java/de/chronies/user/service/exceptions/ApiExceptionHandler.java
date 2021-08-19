package de.chronies.user.service.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiValidationExceptionBase.class)
    public ResponseEntity<Object> handleValidationException(ApiValidationExceptionBase e, HttpServletRequest request) {

        var apiException = ApiValidationException.builder()
                .status(e.getStatus())
                .messages(e.getMessages())
                .time(Instant.now())
                .path(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString())
                .build();

        return new ResponseEntity<>(apiException, apiException.getStatus());
    }


    @ExceptionHandler(ApiExceptionBase.class)
    public ResponseEntity<Object> handleException(ApiExceptionBase e, HttpServletRequest request) {

        var apiException = ApiException.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .time(Instant.now())
                .path(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString())
                .build();

        return new ResponseEntity<>(apiException, apiException.getStatus());
    }

}
