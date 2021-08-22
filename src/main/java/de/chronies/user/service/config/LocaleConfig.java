package de.chronies.user.service.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class LocaleConfig {

    @PostConstruct
    public void init() {

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

        System.out.println("Date for : \"Europe/Berlin\"" + new Date().toString());
    }
}
