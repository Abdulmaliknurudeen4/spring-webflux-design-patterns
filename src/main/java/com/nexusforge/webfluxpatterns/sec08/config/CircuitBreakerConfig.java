package com.nexusforge.webfluxpatterns.sec08.config;

import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfig {

    @Bean
    public CircuitBreakerConfigCustomizer reviewService() {
        return CircuitBreakerConfigCustomizer.of("review-service",
                builder -> {
                    builder.minimumNumberOfCalls(4)
                            .slidingWindowType(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.TIME_BASED);
                });
    }
}
