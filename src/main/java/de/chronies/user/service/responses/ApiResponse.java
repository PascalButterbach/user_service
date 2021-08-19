package de.chronies.user.service.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Builder
@RequiredArgsConstructor
public class ApiResponse {

    private final String message;
    private final HttpStatus status;
    private final Instant time;
    private final String path;

    public String getTime(){
        return DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
                .withLocale(Locale.US)
                .withZone(ZoneId.of("GMT"))
                .format(time);
    }

}
