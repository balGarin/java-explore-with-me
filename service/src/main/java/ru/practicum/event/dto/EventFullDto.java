package ru.practicum.event.dto;

import lombok.Data;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryShortDto;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.State;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

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
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;
    private Integer views;
    private String title;
}
