package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.event.model.Location;

import java.time.LocalDateTime;

@Data
public class EventDtoIn {
    @NotNull
    @Size(min = 20,max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotNull
    @Size(min = 20,max = 7000)
    private String description;
    @NotNull
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private Boolean paid = false;
    private Integer participantLimit =10;
    private Boolean requestModeration = true;
    @NotNull
    @Size(min = 3,max = 120)
    private String title;
}
