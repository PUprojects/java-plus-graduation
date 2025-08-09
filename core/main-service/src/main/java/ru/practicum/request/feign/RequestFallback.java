package ru.practicum.request.feign;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.request.contract.RequestOperations;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
public class RequestFallback implements RequestOperations {
    @Override
    public List<ParticipationRequestDto> getRequestsByEventId(Long eventId) {
        log.debug("Called fallback method getRequestsByEventId");
        return List.of();
    }

    @Override
    public List<ParticipationRequestDto> getByEventIdAndIdIn(Long eventId, List<Long> requestsIds) {
        log.debug("Called fallback method getByEventIdAndIdIn");
        return List.of();
    }

    @Override
    public void updateRequests(List<ParticipationRequestDto> requests) {
        log.debug("Called fallback method updateRequests");
    }
}
