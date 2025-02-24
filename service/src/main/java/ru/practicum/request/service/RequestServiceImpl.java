package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.IncorrectDataException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestMapper;
import ru.practicum.request.dto.RequestStatusUpdateDtoIn;
import ru.practicum.request.dto.RequestStatusUpdateDtoOut;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.Status;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Override
    public RequestDto requestForParticipation(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " wos not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " wos not found"));

        Request request;
        request = requestRepository.findByEventIdAndRequesterId(eventId, userId);
        if (request != null) {
            throw new ConflictException("User =" + userId + " already has a request at Event =" + eventId);
        } else {
            request = new Request();
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("Current user =" + userId + " can not add request at the own event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event =" + eventId + " not published");
        }

        if (event.getParticipantLimit().equals(event.getConfirmedRequests()) && !event.getParticipantLimit().equals(0)) {
            throw new ConflictException("The limit of Event =" + event.getParticipantLimit() + " has been reached");
        }
        request.setCreated(LocalDateTime.now());
        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0)) {
            request.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            request.setStatus(Status.PENDING);
        }
        request.setRequester(user);
        request.setEvent(event);
        log.info("Добавлен новый запрос : {}", request);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getRequestsForOtherUserEvents(Long userId) {
        return requestMapper.toRequestDto(requestRepository.findAllByRequesterIdIs(userId));
    }

    @Override
    public RequestDto cancelOwnRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " wos not found"));
        if (request.getRequester().getId().equals(userId)) {
            request.setStatus(Status.CANCELED);
            log.info("Юзер с id = {} отменил свой запрос - {}", userId, request);
            return requestMapper.toRequestDto(requestRepository.save(request));
        } else {
            throw new ConflictException("User =" + userId + " is not the owner of request=" + userId);
        }
    }

    @Override
    public List<RequestDto> getRequestsForOwnEvent(Long userId, Long eventId) {
        return requestMapper.toRequestDto(requestRepository.findAllByEventId(eventId));
    }

    @Override
    public RequestStatusUpdateDtoOut changeStatusRequests(RequestStatusUpdateDtoIn requestStatusUpdateDtoIn,
                                                          Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " wos not found"));
        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0)) {
            throw new ConflictException("Event =" + event.getId() + "does not need confirmed");
        }
        if (event.getParticipantLimit().equals(event.getConfirmedRequests()) && !event.getParticipantLimit().equals(0)) {
            throw new ConflictException("The participant limit has been reached");
        }
        List<Request> requests = requestRepository
                .findAllByIdInAndEventIdIsOrderByIdAsc(requestStatusUpdateDtoIn.getRequestIds(), eventId);
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();
        if (requestStatusUpdateDtoIn.getStatus().equals(Status.CONFIRMED)) {
            for (Request request : requests) {
                if (request.getStatus().equals(Status.PENDING)) {
                    if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                        request.setStatus(Status.REJECTED);
                        rejectedRequests.add(request);
                    } else {
                        request.setStatus(Status.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        confirmedRequests.add(request);
                    }
                } else {
                    throw new ConflictException("Request status = " + request.getStatus() + " is not pending");
                }
            }
        } else if (requestStatusUpdateDtoIn.getStatus().equals(Status.REJECTED)) {
            for (Request request : requests) {
                if (request.getStatus().equals(Status.PENDING)) {
                    request.setStatus(Status.REJECTED);
                    rejectedRequests.add(request);
                } else {
                    throw new ConflictException("Request status = " + request.getStatus() + " is not pending");
                }
            }
        } else {
            throw new IncorrectDataException("Param =" + requestStatusUpdateDtoIn.getStatus() + " is invalid");
        }
        log.info("Список подтвержденных запрос - {} Список отклоненных запросов - {}", confirmedRequests, rejectedRequests);
        eventRepository.save(event);
        requestRepository.saveAll(requests);
        return requestMapper.toRequestDto(confirmedRequests, rejectedRequests);
    }
}
