package org.gsmapp.configuration;

import io.swagger.v3.oas.models.annotations.OpenAPI31;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;


@Configuration
@OpenAPI31
@RequiredArgsConstructor
public class BeanConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}