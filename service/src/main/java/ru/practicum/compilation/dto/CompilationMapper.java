package ru.practicum.compilation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.model.Event;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = EventMapper.class)
public interface CompilationMapper {
    @Mapping(target = "events", source = "eventsEntity")
    Compilation toCompilation(CompilationDtoIn compilationDtoIn, List<Event> eventsEntity);

    CompilationDtoOut toCompilationDto(Compilation compilation);

    List<CompilationDtoOut> toCompilationDto(List<Compilation> compilations);
}
