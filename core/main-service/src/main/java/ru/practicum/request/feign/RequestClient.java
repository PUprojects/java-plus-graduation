package ru.practicum.request.feign;


import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.request.contract.RequestOperations;

@FeignClient(name = "request-service", path = "api/requests", fallback = RequestFallback.class)
public interface RequestClient extends RequestOperations {
}
