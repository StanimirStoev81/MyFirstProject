package org.example.myfirstproject.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
    @Bean
    public WebClient reviewServiceClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081/api/reviews")
                .build();
    }
}