package ru.practicum.stats_client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private final DiscoveryClient discoveryClient;
    private final RetryTemplate retryTemplate;


    public WebClientConfig(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;

        retryTemplate = RetryTemplate.builder()
                .maxAttempts(3)
                .fixedBackoff(3000)
                .build();
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder,
                               @Value("${stats-server.id:stats-server}") String serviceId) {

        String baseUrl = retryTemplate.execute(cxt -> getStatServerBaseUrl(serviceId));

        return builder
                .baseUrl(baseUrl)
                .build();
    }

    private String getStatServerBaseUrl(String serviceId) {
        ServiceInstance statServiceInstance;
        try {
            statServiceInstance = discoveryClient.getInstances(serviceId).getFirst();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

         return statServiceInstance.getUri().toString();
    }

}