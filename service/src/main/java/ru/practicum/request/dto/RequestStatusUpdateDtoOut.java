package ru.practicum.request.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestStatusUpdateDtoOut {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;

}
