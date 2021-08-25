package de.chronies.user.service.controller;

import de.chronies.user.service.dto.responses.ApiResponseDto;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@ControllerAdvice
public class HeaderModifierAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter,
                            @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter methodParameter,
                                  @NonNull MediaType mediaType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> aClass,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        if(body.getClass() == ApiResponseDto.class)
        {
            ApiResponseDto test = (ApiResponseDto) body;
            System.out.println(test.getStatus().value() + " --- " + test.getStatus());
            response.setStatusCode(test.getStatus());
        }

        response.getHeaders().set(HttpHeaders.DATE, DateTimeFormatter.ofPattern("EEE dd MMM yyyy HH:mm:ss z", Locale.US)
                .withLocale(Locale.GERMANY)
                .withZone(ZoneId.of("Europe/Berlin"))
                .format(Instant.now()));

        return body;
    }
}
