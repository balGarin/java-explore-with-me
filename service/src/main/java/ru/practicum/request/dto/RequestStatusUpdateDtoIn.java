package ru.practicum.request.dto;

import lombok.Data;
import ru.practicum.request.model.Status;

@Data
public class RequestStatusUpdateDtoIn {
    private Long[] requestIds;
    private Status status;
}
