package ru.practicum.event.contract;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Validated
public interface EventOperations {

    @GetMapping(path = "/{eventId}")
    EventFullDto getEventById(@PathVariable long eventId);

    @GetMapping(path = "/short")
    List<EventShortDto> getAllByIdsList(@RequestParam("ids")List<Long> ids);
}
