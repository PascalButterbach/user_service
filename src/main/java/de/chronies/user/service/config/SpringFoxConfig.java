package de.chronies.user.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("de.chronies"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "USER-SERVICE API REST",
                "USER-SERVICE MICROSERVICE - Part of Chronies.",
                "1.0",
                "Terms of Service",
                new Contact("Pascal Butterbach", "https://www.butterbach.org",
                        "pascal@butterbach.org"),
                "APACHE LICENSE, VERSION 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html", new ArrayList<>()
        );

        return apiInfo;
    }
}
