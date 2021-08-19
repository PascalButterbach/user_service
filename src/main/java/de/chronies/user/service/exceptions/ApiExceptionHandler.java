package de.chronies.user.service.exceptions;

import de.chronies.user.service.responses.ApiResponse;
import de.chronies.user.service.responses.ApiValidationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiValidationResponseBase.class)
    public ResponseEntity<Object> handleValidationException(ApiValidationResponseBase e, HttpServletRequest request) {

        var apiException = ApiValidationResponse.builder()
                .status(e.getStatus())
                .messages(e.getMessages())
                .time(Instant.now())
                .path(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString())
                .build();

        return new ResponseEntity<>(apiException, apiException.getStatus());
    }


    @ExceptionHandler(ApiResponseBase.class)
    public ResponseEntity<Object> handleException(ApiResponseBase e, HttpServletRequest request) {

        var apiException = ApiResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .time(Instant.now())
                .path(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString())
                .build();

        return new ResponseEntity<>(apiException, apiException.getStatus());
    }

}
