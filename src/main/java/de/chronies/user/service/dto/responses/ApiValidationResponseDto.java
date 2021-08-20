package de.chronies.user.service.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
@RequiredArgsConstructor
public class ApiValidationResponseDto {

    private final Map<String, String> messages;
    private final HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss z", timezone = "Europe/Berlin", locale = "de")
    private final Instant time;
    private final String path;

}
