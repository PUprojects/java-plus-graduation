package ru.practicum.compilations.event.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.event.contract.EventOperations;

@FeignClient(name = "main-service", path = "api/events", fallback = EventFallback.class)
public interface EventClient extends EventOperations {
}
