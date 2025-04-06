package com.aom.meetime.hubspot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * The AppConfig class is annotated with @Configuration, indicating that it is a source
 * of bean definitions for the application context. This class provides configuration for
 * application-specific beans to support application functionalities.
 */
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
