package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.contract.EventOperations;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "api/events")
@RequiredArgsConstructor
@Validated
public class EventApiController implements EventOperations {
    private final EventService eventService;

    @Override
    public EventFullDto getEventById(long eventId) {
        return eventService.getEventById(eventId);
    }

    @Override
    public List<EventShortDto> getAllByIdsList(List<Long> ids) {
        return eventService.getShortsByIds(ids);
    }
}
