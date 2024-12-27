package ru.practicum.request.dto;

import lombok.Data;
import ru.practicum.request.model.Status;

import java.util.List;

@Data
public class RequestStatusUpdateDtoIn {
    private Long[] requestIds;
    private Status status;
}
