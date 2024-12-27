package ru.practicum.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.request.model.Request;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RequestMapper {
    @Mapping(target = "event",expression = "java(request.getEvent().getId())")
    @Mapping(target = "requester",expression = "java(request.getRequester().getId())")
    RequestDto toRequestDto(Request request);

    List<RequestDto> toRequestDto(List<Request>requests);


    RequestStatusUpdateDtoOut toRequestDto(List<Request>confirmedRequests,List<Request>rejectedRequests);
}
