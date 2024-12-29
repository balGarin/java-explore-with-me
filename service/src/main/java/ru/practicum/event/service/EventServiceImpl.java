package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.Client;
import ru.practicum.StatDtoOut;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.IncorrectDateException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl implements EventService {


    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final Client client;


    public EventServiceImpl(EventRepository eventRepository,
                            EventMapper eventMapper,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            Client client) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.client = client;
    }

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

        if (event.getState().equals(State.CANCELED) || event.getState().equals(State.PENDING)) {
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
                    throw new ConflictException("Invalid eventDate=" + eventUpdateUserDto.getEventDate());
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


            if (eventUpdateUserDto.getStateAction().equals(StateActionUser.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            } else if (eventUpdateUserDto.getStateAction().equals(StateActionUser.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            } else {
                throw new IncorrectDateException("Invalid parameter stateAction=" + eventUpdateUserDto.getStateAction());
            }
        } else {
            throw new ConflictException("Only pending or canceled events can be changed");
        }
        Event eventUpdated = eventRepository.save(event);
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
                throw new ConflictException("Invalid eventDate=" + eventUpdateAdminDto.getEventDate());
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


        if (event.getState().equals(State.PENDING)) {
            if (eventUpdateAdminDto.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (eventUpdateAdminDto.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            } else {
                throw new IncorrectDateException("Param =" + eventUpdateAdminDto.getStateAction() + " is invalid");
            }
        } else {
            throw new ConflictException("Event=" + event.getId() + " is not in pending state");
        }
        return eventMapper.toFullDto(eventRepository.save(event));

    }

    @Override
    public List<EventShortDto> getEvents(EventGetRequestDto eventGetRequestDto, HttpServletRequest request) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new LinkedList<>();
        conditions.add(event.paid.eq(eventGetRequestDto.getPaid()));
        conditions.add(event.annotation.toLowerCase().like(eventGetRequestDto.getText())
                .or(event.description.toLowerCase().like(eventGetRequestDto.getText())));
        conditions.add(event.state.eq(State.PUBLISHED));
        if (eventGetRequestDto.getOnlyAvailable()) {
            conditions.add(event.participantLimit.goe(event.confirmedRequests));
        }
        if (eventGetRequestDto.getRangeStart() != null || eventGetRequestDto.getRangeEnd() != null) {
            conditions.add(event.eventDate.between(eventGetRequestDto.getRangeStart(), eventGetRequestDto.getRangeEnd()));
        } else {
            conditions.add(event.eventDate.after(LocalDateTime.now()));
        }
        conditions.add(event.category.id.in(eventGetRequestDto.getCategories()));
        Sort sort = getSort(eventGetRequestDto.getSort());
        PageRequest pageRequest = PageRequest.of(eventGetRequestDto.getFrom(), eventGetRequestDto.getSize(), sort);
        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and).get();
        Iterable<Event> all = eventRepository.findAll(finalCondition, pageRequest);
        Iterator<Event> iterator = all.iterator();
        List<Event> events = new LinkedList<>();
        while (iterator.hasNext()) {
            events.add(iterator.next());
        }
        String uri = saveStat(events, request);
        getViews(events, uri);
        return eventMapper.toShortDto(events);
    }


    private String saveStat(List<Event> events, HttpServletRequest request) {
        List<String> ids = events.stream()
                .map(Event::getId)
                .map(id -> id.toString())
                .toList();
        String uri = ids.stream().reduce("", (current, next) -> current + "/" + next);
        String finalUri = request.getRequestURI() + uri;
        client.addStat("ewm-main-service", finalUri, request.getRemoteAddr(), LocalDateTime.now());
        return finalUri;
    }

    private void getViews(List<Event> events, String finalUri) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // "events/2/233/44/1
        Map<Long, Event> eventMap = events.stream()
                .collect(Collectors.toMap(Event::getId, event -> event));
        List<String> listUris = eventMap.keySet().stream()
                .map(id -> "/events/" + id).toList();
        String[] uris = listUris.toArray(new String[listUris.size() + 1]);
        uris[uris.length - 1] = finalUri;
        ResponseEntity<Object> response = client.getStats(LocalDateTime.MIN.format(formatter),
                LocalDateTime.MAX.format(formatter), uris, true);
        List<StatDtoOut> stat = (List<StatDtoOut>) response.getBody();
        for (StatDtoOut statDto : stat) {
            String uniUri = statDto.getUri();
            String[] uniUris = uniUri.split("/");
            for (int i = 1; i < uniUris.length; i++) {
                Event event = eventMap.get(Long.getLong(uniUris[i]));
                event.setViews(event.getViews() + statDto.getHits());
            }
        }
        eventRepository.saveAll(eventMap.values());

    }


    private Sort getSort(EventGetRequestDto.Sort sort) {
        if (sort.equals(EventGetRequestDto.Sort.EVENT_DATE)) {
            return Sort.by("eventDate").descending();
        } else {
            return Sort.by("views").descending();
        }
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }


}
