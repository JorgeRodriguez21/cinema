package com.example.cinema.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.web.client.RestTemplate

@Configuration
class BeanConfiguration {
    @Bean()
    @Scope("singleton")
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}