package ru.practicum.request.mapper;

import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.ParticipationRequest;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto toDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEventId())
                .requester(request.getRequesterId())
                .status(request.getStatus())
                .build();
    }

    public static ParticipationRequest toRequest(ParticipationRequestDto dto) {
        return ParticipationRequest.builder()
                .id(dto.getId())
                .created(dto.getCreated())
                .eventId(dto.getEvent())
                .requesterId(dto.getRequester())
                .status(dto.getStatus())
                .build();
    }
}
