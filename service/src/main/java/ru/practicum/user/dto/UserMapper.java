package ru.practicum.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toUser(UserShortDto shortDto);

    UserDto toUserDto(User user);

    List<UserDto> toUserDto(List<User>users);
}
