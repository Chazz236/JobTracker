package com.jobtracker.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI jobTrackerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Tracker API")
                        .description("A RESTful service to track job applications")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Chajeesan")
                                .url("https://github.com/Chazz236")));
    }
}
