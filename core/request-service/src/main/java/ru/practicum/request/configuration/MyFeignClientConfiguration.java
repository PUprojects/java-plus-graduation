package ru.practicum.request.configuration;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.request.event.feign.MyFeignErrorDecoder;

@Configuration
public class MyFeignClientConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new MyFeignErrorDecoder();
    }
}
