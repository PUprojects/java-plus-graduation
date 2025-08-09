package ru.practicum.request.contract;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

@Validated
public interface RequestOperations {
    @GetMapping("/event/{eventId}")
    List<ParticipationRequestDto> getRequestsByEventId(@PathVariable Long eventId);

    @GetMapping("/event/{eventId}/requests")
    List<ParticipationRequestDto> getByEventIdAndIdIn(@PathVariable Long eventId,
                                                      @RequestParam("requestsIds") List<Long> requestsIds);

    @PostMapping
    void updateRequests(@RequestBody List<ParticipationRequestDto> requests);
}
