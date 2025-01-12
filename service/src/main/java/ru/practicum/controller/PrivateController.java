package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDtoIn;
import ru.practicum.event.dto.EventUpdateUserDto;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.RequestStatusUpdateDtoIn;
import ru.practicum.request.service.RequestService;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class PrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("/{userId}/events")
    public ResponseEntity<Object> addNewEvent(@RequestBody @Valid EventDtoIn eventDtoIn,
                                              @PathVariable(name = "userId") Long userId) {
        log.warn("Получен запрос на добавление нового события - {}", eventDtoIn);
        return ResponseEntity.status(201).body(eventService.addNewEvent(eventDtoIn, userId));

    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<Object> getEventsByCurrentUser(@PathVariable(name = "userId") Long userId,
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск событий текущего пользователя с Id = {}, и RequestParams : {},{}",
                userId, from, size);
        return ResponseEntity.status(200).body(eventService.getEventsOfCurrentUser(userId, from, size));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> getFullInformationOfEventByCurrentUser(@PathVariable(name = "userId") Long userId,
                                                                         @PathVariable(name = "eventId") Long eventId) {
        log.info("Получен запрос на поиск полной информации о событие с Id= {}, добавленного текущим пользователем с Id = {}",
                eventId, userId);
        return ResponseEntity.status(200).body(eventService.getFullInformationOfEventByCurrentUser(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> updateEventByCurrentUser(@RequestBody @Valid EventUpdateUserDto eventUpdateUserDto,
                                                           @PathVariable(name = "userId") Long userId,
                                                           @PathVariable(name = "eventId") Long eventId) {
        log.info("Получен запрос на обновление события с Id= {},  пользователем с Id= {}, с RequestBody - {}",
                eventId, userId, eventUpdateUserDto);
        return ResponseEntity.status(200).body(eventService
                .updateEventByCurrentUser(eventUpdateUserDto, userId, eventId));
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<Object> requestForParticipation(@PathVariable(name = "userId") Long userId,
                                                          @RequestParam(name = "eventId") Long eventId) {
        log.warn("Получен запрос на запрос участия в событие с Id= {}, пользователем с Id= {}", eventId, userId);
        return ResponseEntity.status(201).body(requestService.requestForParticipation(userId, eventId));
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<Object> getRequestsForOtherUsersEvents(@PathVariable(name = "userId") Long userId) {
        log.info("""
                        Получен запрос на поиск запросов на участие в событиях добавленных другими пользователями, от юзера с Id= {}""",
                userId);
        return ResponseEntity.status(200).body(requestService.getRequestsForOtherUserEvents(userId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelOwnRequest(@PathVariable(name = "userId") Long userId,
                                                   @PathVariable(name = "requestId") Long requestId) {
        log.info("Получен запрос на отмену своего запроса с Id= {}, от пользователя с Id= {}", requestId, userId);
        return ResponseEntity.status(200).body(requestService.cancelOwnRequest(userId, requestId));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> getRequestsForOwnEvent(@PathVariable(name = "userId") Long userId,
                                                         @PathVariable(name = "eventId") Long eventId) {
        log.info("Получен запрос на поиск запросов оставленных  пользователем с Id= {}, к событию с Id= {} ", userId, eventId);
        return ResponseEntity.status(200).body(requestService.getRequestsForOwnEvent(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> changeStatusRequests(@RequestBody RequestStatusUpdateDtoIn requestStatusUpdateDtoIn,
                                                       @PathVariable(name = "userId") Long userId,
                                                       @PathVariable(name = "eventId") Long eventId) {
        log.info("""
                Получен запрос на изменение статуса от пользователя с Id= {},
                 к событию с Id= {}, и RequestBody - {}""", userId, eventId, requestStatusUpdateDtoIn);
        return ResponseEntity.status(200)
                .body(requestService.changeStatusRequests(requestStatusUpdateDtoIn, userId, eventId));
    }
}
