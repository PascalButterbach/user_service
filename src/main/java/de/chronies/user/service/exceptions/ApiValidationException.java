package de.chronies.user.service.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Getter
@Builder
@RequiredArgsConstructor
public class ApiValidationException {

    private final Map<String, String> messages;
    private final HttpStatus status;
    private final Instant time;
    private final String path;

    public String getTime() {
        return DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
                .withLocale(Locale.US)
                .withZone(ZoneId.of("GMT"))
                .format(time);
    }

}
