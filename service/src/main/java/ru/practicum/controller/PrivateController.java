package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDtoIn;
import ru.practicum.event.dto.EventUpdateAdminDto;
import ru.practicum.event.dto.EventUpdateUserDto;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.RequestStatusUpdateDtoIn;
import ru.practicum.request.service.RequestService;

@RestController
@RequestMapping("/users")
@Slf4j
public class PrivateController {

    private final EventService eventService;

    private final RequestService requestService;

    public PrivateController(EventService eventService, RequestService requestService) {
        this.eventService = eventService;
        this.requestService = requestService;
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<Object> addNewEvent(@RequestBody @Valid EventDtoIn eventDtoIn,
                                              @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.status(201).body(eventService.addNewEvent(eventDtoIn, userId));

    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<Object> getEventsByCurrentUser(@PathVariable(name = "userId") Long userId,
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.status(200).body(eventService.getEventsOfCurrentUser(userId, from, size));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> getFullInformationOfEventByCurrentUser(@PathVariable(name = "userId") Long userId,
                                                                         @PathVariable(name = "eventId") Long eventId) {
        return ResponseEntity.status(200).body(eventService.getFullInformationOfEventByCurrentUser(userId, eventId));
    }


    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> updateEventByCurrentUser(@RequestBody @Valid EventUpdateUserDto eventUpdateUserDto,
                                                           @PathVariable(name = "userId") Long userId,
                                                           @PathVariable(name = "eventId") Long eventId) {
        return ResponseEntity.status(200).body(eventService
                .updateEventByCurrentUser(eventUpdateUserDto, userId, eventId));

    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<Object> requestForParticipation(@PathVariable(name = "userId") Long userId,
                                                          @RequestParam(name = "eventId") Long eventId) {
        log.warn("params : userId= {}, eventId= {}", userId, eventId);
        return ResponseEntity.status(201).body(requestService.requestForParticipation(userId, eventId));
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<Object> getRequestsForOtherUserEvents(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.status(200).body(requestService.getRequestsForOtherUserEvents(userId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelOwnRequest(@PathVariable(name = "userId") Long userId,
                                                   @PathVariable(name = "requestId") Long requestId) {
        return ResponseEntity.status(200).body(requestService.cancelOwnRequest(userId, requestId));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> getRequestsForOwnEvent(@PathVariable(name = "userId") Long userId,
                                                         @PathVariable(name = "eventId") Long eventId) {

        return ResponseEntity.status(200).body(requestService.getRequestsForOwnEvent(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object>changeStatusRequests(@RequestBody RequestStatusUpdateDtoIn requestStatusUpdateDtoIn,
                                                      @PathVariable(name = "userId") Long userId,
                                                      @PathVariable(name = "eventId") Long eventId){
        return ResponseEntity.status(200)
                .body(requestService.changeStatusRequests(requestStatusUpdateDtoIn,userId,eventId));

    }


}
