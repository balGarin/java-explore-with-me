package ru.practicum.comment.dto;

import lombok.Data;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserDto;

@Data
public class CommentDtoOut {
    private long id;
    private String text;
    private UserDto author;
    private EventShortDto event;
    private String created;
}
