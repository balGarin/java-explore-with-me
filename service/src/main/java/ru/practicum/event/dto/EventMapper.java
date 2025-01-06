package ru.practicum.event.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserMapper;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class, UserMapper.class},
        imports = Location.class)
public interface EventMapper {
    @Mapping(target = "lat", expression = "java(eventDtoIn.getLocation().getLat())")
    @Mapping(target = "lon", expression = "java(eventDtoIn.getLocation().getLon())")
    @Mapping(target = "category", ignore = true)
    Event toEvent(EventDtoIn eventDtoIn);


    @Mapping(target = "location", expression = "java(new Location(event.getLat(),event.getLon()))")
    @Mapping(target = "createdOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "publishedOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventFullDto toFullDto(Event event);


    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventShortDto toShortDto(Event event);

    List<EventShortDto> toShortDto(List<Event> events);

    List<EventFullDto> toFullDto(List<Event> events);

}
