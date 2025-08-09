package ru.practicum.request.user.feign;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.user.contract.UserOperations;
import ru.practicum.user.dto.UserShortDto;

@Slf4j
public class UserFallback implements UserOperations {

    @Override
    public UserShortDto getUser(long userId) {
        log.debug("Called fallback method getUser");
        return new UserShortDto(-1L, "Ошибка обращения к сервису пользователей");
    }

    @Override
    public Boolean isUserExist(long userId) {
        log.debug("Called fallback method isUserExist");
        return Boolean.FALSE;
    }
}
