package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;

import java.util.List;

public interface UserService {
    UserDto annNewUser(UserShortDto userShortDto);

    List<UserDto> getAllUsers(Long[] ids, Integer from, Integer size);

    void removeUser(Long userId);

}
