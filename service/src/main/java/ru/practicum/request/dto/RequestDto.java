package ru.practicum.request.dto;

import lombok.Data;
import ru.practicum.request.model.Status;


@Data
public class RequestDto {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private Status status;
}
