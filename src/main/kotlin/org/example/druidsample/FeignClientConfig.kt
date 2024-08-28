package org.example.druidsample

import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignClientConfig {
    @Bean
    fun errorDecoder(): ErrorDecoder {
        return FeignClientErrorDecoder()
    }
}
