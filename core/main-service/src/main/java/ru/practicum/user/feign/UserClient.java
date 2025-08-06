package ru.practicum.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.user.contract.UserOperations;

@FeignClient(name = "user", path = "/api/users", fallback = UserFallback.class)
public interface UserClient extends UserOperations {
}
