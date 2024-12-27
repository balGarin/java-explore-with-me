package ru.practicum.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
import java.util.List;

@Service
@Slf4j
public class EventServiceImpl implements EventService {


    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;


    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
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


    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }


}
