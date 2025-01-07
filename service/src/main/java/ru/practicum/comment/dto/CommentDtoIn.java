package ru.practicum.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDtoIn {
    @NotNull
    @NotBlank
    @Size(max = 2000)
    private String text;
    private Boolean anonymous;
    @NotNull
    private Long eventId;
}
