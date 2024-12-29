package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.event.dto.*;

import java.net.http.HttpRequest;
import java.util.List;

public interface EventService {
    EventFullDto addNewEvent(EventDtoIn eventDtoIn, Long userId);

    List<EventShortDto> getEventsOfCurrentUser(Long userId, Integer from, Integer size);

    EventFullDto getFullInformationOfEventByCurrentUser(Long userId, Long eventId);

    EventFullDto updateEventByCurrentUser(EventUpdateUserDto eventUpdateUserDto, Long userId, Long eventId);

    EventFullDto editEventByAdmin(EventUpdateAdminDto eventUpdateAdminDto, Long eventId);

    List<EventShortDto> getEvents(EventGetRequestDto eventGetRequestDto, HttpServletRequest request);
}
