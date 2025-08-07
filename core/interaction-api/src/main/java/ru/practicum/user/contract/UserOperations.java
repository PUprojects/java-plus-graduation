package ru.practicum.user.contract;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.user.dto.UserShortDto;

@Validated
public interface UserOperations {
    @GetMapping(path = "/{userId}")
    UserShortDto getUser(@PathVariable long userId);

    @GetMapping(path = "/exist/{userId}")
    Boolean isUserExist(@PathVariable long userId);
}
