package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.service.CommentService;
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

    private final CommentService commentService;

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

    @PostMapping("/{userId}/comments")
    public ResponseEntity<Object> addNewComment(@PathVariable(name = "userId") Long userId,
                                                @RequestBody @Valid CommentDtoIn commentDtoIn) {
        log.info("Получен запрос на добавление нового комментария - {}", commentDtoIn);
        return ResponseEntity.status(201).body(commentService.addNewComment(userId, commentDtoIn));
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Object> editOwnComment(@PathVariable(name = "userId") Long userId,
                                                 @PathVariable(name = "commentId") Long commentId,
                                                 @RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        log.info("Получен запрос на обновление комментария с Id= {} на - {}", commentId, commentUpdateDto);
        return ResponseEntity.status(200).body(commentService.editOwnComment(userId, commentId, commentUpdateDto));
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Object> deleteOwnComment(@PathVariable(name = "userId") Long userId,
                                                   @PathVariable(name = "commentId") Long commentId) {
        log.info("Получен запрос на удаление комментария с Id= {}", commentId);
        commentService.deleteOwnComment(userId, commentId);
        return ResponseEntity.status(204).body("Комментарий удален");

    }

    @GetMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Object> getCommentByIdOfCurrentUser(@PathVariable(name = "userId") Long userId,
                                                              @PathVariable(name = "commentId") Long commentId) {
        log.info("Получен запрос на получение комментария с Id= {}", commentId);
        return ResponseEntity.status(200).body(commentService.getCommentByIdOfCurrentUser(userId, commentId));
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<Object> getAllCommentsByCurrentUser(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск комментариев с параметрами from= {},size= {}", from, size);
        return ResponseEntity.status(200).body(commentService.getAllCommentsByCurrentUser(userId, from, size));
    }
}
