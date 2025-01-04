package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto annNewUser(UserShortDto userShortDto) {
        User user = userRepository.save(userMapper.toUser(userShortDto));
        log.info("Добавлен новый Юзер - {}", user);
        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers(Long[] ids, Integer from, Integer size) {
        if (ids == null) {
            log.info("Запрос на получения списка юзеров без списка Ids");
            return userMapper.toUserDto(userRepository.findAllWithoutIds(from, size));
        } else {
            log.info("Запрос на получения списка юзеров со списком Ids");
            return userMapper.toUserDto(userRepository.findAllInIds(ids, from, size));
        }
    }

    @Override
    public void removeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " wos not found"));
        log.info("Юзер - {} был успешно удален", user);
        userRepository.delete(user);
    }
}
