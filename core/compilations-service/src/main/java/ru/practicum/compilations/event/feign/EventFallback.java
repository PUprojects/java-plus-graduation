package ru.practicum.compilations.event.feign;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.event.contract.EventOperations;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Slf4j
public class EventFallback implements EventOperations {
    @Override
    public EventFullDto getEventById(long eventId) {
        log.debug("Called fallback method getEventById");
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(-1L);
        eventFullDto.setAnnotation("Ошибка обращения к сервису событий (event-service)");
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getAllByIdsList(List<Long> ids) {
        log.debug("Called fallback method getAllByIdsList");
        return List.of();
    }
}
