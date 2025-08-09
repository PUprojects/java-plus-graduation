package ru.practicum.request.event.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.request.configuration.MyFeignClientConfiguration;
import ru.practicum.event.contract.EventOperations;

@FeignClient(name = "main-service", path = "api/events", fallback = EventFallback.class, configuration = MyFeignClientConfiguration.class)
public interface EventClient extends EventOperations {
}
