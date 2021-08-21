package de.chronies.user.service.exceptions;

import de.chronies.user.service.dto.responses.ApiResponseDto;
import de.chronies.user.service.dto.responses.ApiValidationResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiValidationException.class)
    public ResponseEntity<ApiValidationResponseDto> handleValidationException(ApiValidationException e, HttpServletResponse response, HttpServletRequest request) {

        modifyResponse(response);

        var apiException = ApiValidationResponseDto.builder()
                .status(e.getStatus())
                .messages(e.getMessages())
                .time(Instant.now())
                .path(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString())
                .build();

        return new ResponseEntity<>(apiException, apiException.getStatus());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponseDto> handleException(ApiException e, HttpServletResponse response, HttpServletRequest request) {

        modifyResponse(response);

        var apiException = ApiResponseDto.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .time(Instant.now())
                .path(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString())
                .build();

        return new ResponseEntity<>(apiException, apiException.getStatus());
    }

    private void modifyResponse(HttpServletResponse response) {
        response.setHeader(HttpHeaders.DATE, DateTimeFormatter.ofPattern("EEE dd MMM yyyy HH:mm:ss z", Locale.US)
                .withLocale(Locale.GERMANY)
                .withZone(ZoneId.of("Europe/Berlin"))
                .format(Instant.now()));
    }
}
