package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.event.dto.*;

import java.util.List;

public interface EventService {
    EventFullDto addNewEvent(EventDtoIn eventDtoIn, Long userId);

    List<EventShortDto> getEventsOfCurrentUser(Long userId, Integer from, Integer size);

    EventFullDto getFullInformationOfEventByCurrentUser(Long userId, Long eventId);

    EventFullDto updateEventByCurrentUser(EventUpdateUserDto eventUpdateUserDto, Long userId, Long eventId);

    EventFullDto editEventByAdmin(EventUpdateAdminDto eventUpdateAdminDto, Long eventId);

    List<EventShortDto> getEvents(String text, Integer[] categories, Boolean paid, String rangeStart,
                                  String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size,
                                  HttpServletRequest request);

    EventFullDto getEventById(Long eventId, HttpServletRequest request);

    List<EventFullDto> getEventsByAdmin(Long[] users, String[] states, Long[] categories,
                                        String rangeStart, String rangeEnd, Integer from, Integer size);
}
