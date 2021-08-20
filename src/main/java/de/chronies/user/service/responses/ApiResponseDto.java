package de.chronies.user.service.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Builder
@RequiredArgsConstructor
public class ApiResponseDto {

    private final String message;
    private final HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss z", timezone = "Europe/Berlin")
    private final Instant time;
    private final String path;

}
