package ru.practicum.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto annNewUser(UserShortDto userShortDto) {
        User user = userRepository.save(userMapper.toUser(userShortDto));
        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers(Long[] ids, Integer from, Integer size) {
        if (ids == null) {
            return userMapper.toUserDto(userRepository.findAllWithoutIds(from, size));
        } else {
            return userMapper.toUserDto(userRepository.findAllInIds(ids, from, size));
        }
    }

    @Override
    public void removeUser(Long userId) {
         User user = userRepository.findById(userId)
                 .orElseThrow(()->new NotFoundException("User with id="+userId+" wos not found"));
         userRepository.delete(user);
    }
}
