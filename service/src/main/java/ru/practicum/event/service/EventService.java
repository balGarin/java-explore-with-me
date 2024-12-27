package ru.practicum.event.service;

import ru.practicum.event.dto.*;

import java.util.List;

public interface EventService {
    EventFullDto addNewEvent(EventDtoIn eventDtoIn, Long userId);

    List<EventShortDto> getEventsOfCurrentUser(Long userId, Integer from, Integer size);

    EventFullDto getFullInformationOfEventByCurrentUser(Long userId, Long eventId);

    EventFullDto updateEventByCurrentUser(EventUpdateUserDto eventUpdateUserDto, Long userId, Long eventId);

    EventFullDto editEventByAdmin(EventUpdateAdminDto eventUpdateAdminDto, Long eventId);

}
