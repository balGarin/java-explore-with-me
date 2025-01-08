package ru.practicum.event.dto;

import lombok.Data;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.State;
import ru.practicum.user.dto.UserDto;

import java.util.List;


@Data
public class EventFullDto {
    private Long id;
    private String annotation;
    private String description;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String eventDate;
    private UserDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    private Integer views;
    private String title;
    private Boolean blockComments;
    private List<CommentShortDto> comments;
}
