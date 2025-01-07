package ru.practicum.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserDto;

@Data
public class CommentDtoOut {
 private long id;
 private String Text;
 private UserDto commentator;
 private EventShortDto event;
 private String created;
}
