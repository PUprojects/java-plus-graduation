package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserParam;
import ru.practicum.user.dto.UserShortDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserParam params);

    UserDto updateUser(UserParam params);

    UserDto findUserById(Long userId);

    UserShortDto findShortUserById(Long userId);

    Collection<UserDto> findUsersById(UserParam params);

    UserDto findUserByEmail(UserParam params);

    void deleteUser(UserParam params);

    Boolean isUserExistById(Long userId);
}
