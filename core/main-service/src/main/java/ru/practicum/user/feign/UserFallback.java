package ru.practicum.user.feign;

import ru.practicum.user.contract.UserOperations;
import ru.practicum.user.dto.UserShortDto;

public class UserFallback implements UserOperations {

    @Override
    public UserShortDto getUser(long userId) {
        return new UserShortDto(-1L, "Ошибка обращения к сервису пользователей");
    }

    @Override
    public Boolean isUserExist(long userId) {
        return Boolean.FALSE;
    }
}
