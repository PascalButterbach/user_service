package de.chronies.user.service.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@RequiredArgsConstructor
public class ApiException {

    private final String message;
    private final HttpStatus status;
    private final ZonedDateTime time;
    private final String path;

    public String getTime(){
        return time.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
