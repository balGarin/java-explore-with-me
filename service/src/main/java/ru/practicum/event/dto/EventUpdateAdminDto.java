package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.StateActionAdmin;
import ru.practicum.event.model.StateActionUser;

import java.time.LocalDateTime;

@Data
public class EventUpdateAdminDto {
    private String annotation;
    private Long category;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid ;
    private Integer participantLimit ;
    private Boolean requestModeration;
    private StateActionAdmin stateAction;
    private String title;
}
