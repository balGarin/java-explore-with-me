package ru.practicum.request.service;

import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestStatusUpdateDtoIn;
import ru.practicum.request.dto.RequestStatusUpdateDtoOut;

import java.util.List;

public interface RequestService {
    RequestDto requestForParticipation(Long userId, Long eventId);

    List<RequestDto> getRequestsForOtherUserEvents(Long userId);

    RequestDto cancelOwnRequest(Long userId, Long requestId);

    List<RequestDto> getRequestsForOwnEvent(Long userId, Long eventId);

    RequestStatusUpdateDtoOut changeStatusRequests(RequestStatusUpdateDtoIn requestStatusUpdateDtoIn, Long userId, Long eventId);

}
