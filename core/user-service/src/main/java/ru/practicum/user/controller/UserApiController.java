package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.contract.UserOperations;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.service.UserService;

@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
public class UserApiController implements UserOperations {

    private final UserService userService;

    @Override
    public UserShortDto getUser(long userId) {
        return userService.findShortUserById(userId);
    }

    @Override
    public Boolean isUserExist(long userId) {
        return userService.isUserExistById(userId);
    }
}
