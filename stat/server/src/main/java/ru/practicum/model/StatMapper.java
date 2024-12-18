package ru.practicum.model;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.StatDtoIn;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatMapper {

    Stat fromDto(StatDtoIn statDtoIn);



}
