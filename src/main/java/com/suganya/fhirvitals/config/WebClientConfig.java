package com.suganya.fhirvitals.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// @Configuration tells Spring: "this class defines reusable objects
// (beans) that other parts of the app can ask for."
@Configuration
public class WebClientConfig {

    // @Bean means: "build this object once, and hand the SAME instance
    // to anything in the app that asks for a WebClient."
    // This is dependency injection - the core idea behind Spring.
    @Bean
    public WebClient hapiFhirWebClient() {
        return WebClient.builder()
                .baseUrl("https://hapi.fhir.org/baseR4")
                .defaultHeader("Content-Type", "application/fhir+json")
                .build();
    }
}