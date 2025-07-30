package ru.practicum.stats_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import ru.practicum.stats_dto.CreateHitDto;
import ru.practicum.stats_dto.CreateStatsDto;
import ru.practicum.stats_dto.ResponseStatsDto;

import java.util.List;

@Component
public class StatsClientImpl implements StatsClient {

    private final DiscoveryClient discoveryClient;
    private final RetryTemplate retryTemplate;

    @Value("${stats-server.id:stats-server}")
    private String statServiceId;

    public StatsClientImpl(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;

        retryTemplate = RetryTemplate.builder()
                .maxAttempts(3)
                .fixedBackoff(3000)
                .build();
    }

    @Override
    public void sendHit(CreateHitDto hitDto) {
        getWebClient().post()
                .uri("/hit")
                .bodyValue(hitDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @Override
    public List<ResponseStatsDto> getStats(CreateStatsDto statsDto) {
        return getWebClient().get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder
                            .path("/stats")
                            .queryParam("start", statsDto.getStart())
                            .queryParam("end", statsDto.getEnd());

                    if (statsDto.getUris() != null) {
                        for (String uri : statsDto.getUris()) {
                            builder.queryParam("uris", uri);
                        }
                    }

                    if (Boolean.TRUE.equals(statsDto.getUnique())) {
                        builder.queryParam("unique", true);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToFlux(ResponseStatsDto.class)
                .collectList()
                .block();
    }


    public Mono<Boolean> checkIp(String ip, String uri) {
        return getWebClient().get().uri(uriBuilder -> uriBuilder
                        .path("/stats/check")
                        .queryParam("ip", ip)
                        .queryParam("uri", uri)
                        .build()
                ).retrieve()
                .bodyToMono(Boolean.class);
    }

    private String getStatServerBaseUrl() {
        ServiceInstance statServiceInstance;
        try {
            statServiceInstance = discoveryClient.getInstances(statServiceId).getFirst();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

         return statServiceInstance.getUri().toString();
    }

    private WebClient getWebClient() {
        String baseUrl = retryTemplate.execute(cxt -> getStatServerBaseUrl());
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}