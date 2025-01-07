package ru.practicum.comment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentUpdateDto {
    @Size(max = 2000)
    private String text;
    private Boolean anonymous;
    @NotNull
    private Long eventId;
}
