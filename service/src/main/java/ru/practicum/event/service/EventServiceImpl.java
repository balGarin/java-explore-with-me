package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import ru.practicum.event.model.*;
import ru.practicum.Client;
import ru.practicum.StatDtoOut;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.IncorrectDataException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final Client client;

    @Override
    public EventFullDto addNewEvent(EventDtoIn eventDtoIn, Long userId) {
        if (eventDtoIn.getEventDate().isBefore(getCurrentTime().plusHours(2))) {
            throw new ConflictException("Invalid eventDate:" + eventDtoIn.getEventDate());
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " wos not found"));
        Category category = categoryRepository.findById(eventDtoIn.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id=" + eventDtoIn.getCategory() + " was not found"));
        Event event = eventMapper.toEvent(eventDtoIn);
        event.setInitiator(user);
        event.setCategory(category);
        log.info("Добавлено новое событие - {}", event);
        return eventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getEventsOfCurrentUser(Long userId, Integer from, Integer size) {
        return eventMapper.toShortDto(eventRepository.findAllEventsByCurrentUser(userId, from, size));
    }

    @Override
    public EventFullDto getFullInformationOfEventByCurrentUser(Long userId, Long eventId) {
        return eventMapper.toFullDto(eventRepository.findByInitiatorIdAndId(userId, eventId));
    }

    @Override
    public EventFullDto updateEventByCurrentUser(EventUpdateUserDto eventUpdateUserDto, Long userId, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " wos not found"));
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }
        if (eventUpdateUserDto.getAnnotation() != null) event.setAnnotation(eventUpdateUserDto.getAnnotation());
        if (eventUpdateUserDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventUpdateUserDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            "Category with id=" + eventUpdateUserDto.getCategory() + " was not found"));
            event.setCategory(category);
        }
        if (eventUpdateUserDto.getDescription() != null) event.setDescription(eventUpdateUserDto.getDescription());
        if (eventUpdateUserDto.getEventDate() != null) {
            if (eventUpdateUserDto.getEventDate().isBefore(getCurrentTime().plusHours(2))) {
                throw new IncorrectDataException("Invalid eventDate=" + eventUpdateUserDto.getEventDate());
            }
            event.setEventDate(eventUpdateUserDto.getEventDate());
        }
        if (eventUpdateUserDto.getLocation() != null) {
            Location location = eventUpdateUserDto.getLocation();
            event.setLat(location.getLat());
            event.setLon(location.getLon());
        }
        if (eventUpdateUserDto.getPaid() != null) event.setPaid(eventUpdateUserDto.getPaid());
        if (eventUpdateUserDto.getParticipantLimit() != null) event
                .setParticipantLimit(eventUpdateUserDto.getParticipantLimit());
        if (eventUpdateUserDto.getRequestModeration() != null) event
                .setRequestModeration(eventUpdateUserDto.getRequestModeration());
        if (eventUpdateUserDto.getTitle() != null) event.setTitle(eventUpdateUserDto.getTitle());

        if (eventUpdateUserDto.getStateAction() != null) {
            if (eventUpdateUserDto.getStateAction().equals(StateActionUser.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            } else if (eventUpdateUserDto.getStateAction().equals(StateActionUser.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            } else {
                throw new IncorrectDataException("Invalid parameter stateAction=" + eventUpdateUserDto.getStateAction());
            }
        }

        Event eventUpdated = eventRepository.save(event);
        log.info("Событие с Id = {}, обновлен текущим пользователем на - {}", eventId, eventUpdated);
        return eventMapper.toFullDto(eventUpdated);
    }

    @Override
    public EventFullDto editEventByAdmin(EventUpdateAdminDto eventUpdateAdminDto, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " wos not found"));
        if (event.getEventDate().isBefore(getCurrentTime().plusHours(1))) {
            throw new ConflictException("EventDate=" + event.getEventDate() + " is before than time publication");
        }
        if (eventUpdateAdminDto.getAnnotation() != null) event.setAnnotation(eventUpdateAdminDto.getAnnotation());
        if (eventUpdateAdminDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventUpdateAdminDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            "Category with id=" + eventUpdateAdminDto.getCategory() + " was not found"));
            event.setCategory(category);
        }
        if (eventUpdateAdminDto.getDescription() != null) event.setDescription(eventUpdateAdminDto.getDescription());
        if (eventUpdateAdminDto.getEventDate() != null) {
            if (eventUpdateAdminDto.getEventDate().isBefore(getCurrentTime().plusHours(2))) {
                throw new IncorrectDataException("Invalid eventDate=" + eventUpdateAdminDto.getEventDate());
            }
            event.setEventDate(eventUpdateAdminDto.getEventDate());
        }
        if (eventUpdateAdminDto.getLocation() != null) {
            Location location = eventUpdateAdminDto.getLocation();
            event.setLat(location.getLat());
            event.setLon(location.getLon());
        }
        if (eventUpdateAdminDto.getPaid() != null) event.setPaid(eventUpdateAdminDto.getPaid());
        if (eventUpdateAdminDto.getParticipantLimit() != null) event
                .setParticipantLimit(eventUpdateAdminDto.getParticipantLimit());
        if (eventUpdateAdminDto.getRequestModeration() != null) event
                .setRequestModeration(eventUpdateAdminDto.getRequestModeration());
        if (eventUpdateAdminDto.getTitle() != null) event.setTitle(eventUpdateAdminDto.getTitle());

        if (eventUpdateAdminDto.getStateAction() != null) {
            if (event.getState().equals(State.PENDING)) {
                if (eventUpdateAdminDto.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else if (eventUpdateAdminDto.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
                    event.setState(State.CANCELED);
                } else {
                    throw new IncorrectDataException("Param =" + eventUpdateAdminDto.getStateAction() + " is invalid");
                }
            } else {
                throw new ConflictException("Event=" + event.getId() + " is not in pending state");
            }
        }
        log.info("Событие с Id = {}, обновлен Администратором на - {}", eventId, event);
        return eventMapper.toFullDto(eventRepository.save(event));

    }

    @Override
    public List<EventShortDto> getEvents(String text, Integer[] categories, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size,
                                         HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new LinkedList<>();
        if (paid != null) {
            conditions.add(event.paid.eq(paid));
        }
        if (text != null) {
            conditions.add(event.annotation.toLowerCase().like(text.toLowerCase())
                    .or(event.description.toLowerCase().like(text.toLowerCase())));
        }
        conditions.add(event.state.eq(State.PUBLISHED));
        if (onlyAvailable != null && onlyAvailable) {
            conditions.add(event.participantLimit.goe(event.confirmedRequests));
        }
        if (rangeStart != null && rangeEnd != null) {
            LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
            LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
            if (end.isBefore(start)) {
                throw new IncorrectDataException("Time rangeStart and rangeEnd are invalid");
            }
            conditions.add(event.eventDate.between(start, end));
        } else {
            conditions.add(event.eventDate.after(LocalDateTime.now()));
        }
        if (categories != null) {
            conditions.add(event.category.id.in(categories));
        }
        PageRequest pageRequest;
        if (sort == null) {
            pageRequest = PageRequest.of(from, size);
        } else {
            pageRequest = PageRequest.of(from, size,
                    getSort(SortOf.valueOf(sort)));

        }
        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and).get();
        Page<Event> all = eventRepository.findAll(finalCondition, pageRequest);
        List<Event> events = all.stream().toList();
        saveStat(events, request);
        getViews(events);
        log.info("Список событий : {}", events);
        return eventMapper.toShortDto(events);
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " wos not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Event with id=" + eventId + " wos not found");
        }
        saveStat(List.of(event), request);
        getViews(List.of(event));
        log.info("Найдено событие - {}", event);
        return eventMapper.toFullDto(event);
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(Long[] users, String[] states, Long[] categories, String rangeStart,
                                               String rangeEnd, Integer from, Integer size) {
        QEvent event = QEvent.event;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<BooleanExpression> conditions = new LinkedList<>();
        if (users != null) {
            conditions.add(event.initiator.id.in(users));
        }
        if (states != null) {
            State[] statesEnum = getStates(states);
            conditions.add(event.state.in(statesEnum));
        }
        if (categories != null) {
            conditions.add(event.category.id.in(categories));
        }
        if (rangeStart != null && rangeEnd != null) {
            conditions.add(event.eventDate.between(LocalDateTime.parse(rangeStart, formatter),
                    LocalDateTime.parse(rangeEnd, formatter)));
        } else {
            conditions.add(event.eventDate.after(LocalDateTime.now()));
        }
        PageRequest page = PageRequest.of(from, size);
        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and).get();
        Page<Event> all = eventRepository.findAll(finalCondition, page);
        List<Event> events = all.stream().toList();
        log.info("Список событий : {}", events);
        return eventMapper.toFullDto(events);
    }

    private void saveStat(List<Event> events, HttpServletRequest request) {
        client.addStat("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
    }

    private void getViews(List<Event> events) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<StatDtoOut> stat = client.getStats(LocalDateTime.now().minusYears(100).format(formatter),
                LocalDateTime.now().format(formatter), null, true);
        for (Event event : events) {
            Long hits = 0L;
            for (StatDtoOut statDto : stat) {
                String uri = statDto.getUri();
                if (uri.endsWith(event.getId().toString()) || uri.contains("/" + event.getId() + "/")) {
                    hits += statDto.getHits();
                }
            }
            event.setViews(hits);
        }
        eventRepository.saveAll(events);
    }


    private Sort getSort(SortOf sort) {
        if (sort.equals(SortOf.EVENT_DATE)) {
            return Sort.by("eventDate").descending();
        } else {
            return Sort.by("views").descending();
        }
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    private State[] getStates(String[] stateStrings) {
        State[] states = new State[stateStrings.length];
        try {
            for (int i = 0; i < stateStrings.length; i++) {
                State state = State.valueOf(stateStrings[i]);
                states[i] = state;
            }
        } catch (IllegalArgumentException e) {
            throw new IncorrectDataException(e.getMessage());
        }
        return states;
    }
}
