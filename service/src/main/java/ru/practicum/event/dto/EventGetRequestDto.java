package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class EventGetRequestDto {
    private String text;
    private Integer[]categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private Sort sort;
    private Integer from;
    private Integer size;



    public boolean hasCategories(){
        return categories!=null;
    }

    public enum Sort{
        EVENT_DATE, VIEWS
    }
}

