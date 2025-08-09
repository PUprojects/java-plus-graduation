package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.mapper.ParticipationRequestMapper;
import ru.practicum.request.repository.ParticipationRequestRepository;
import ru.practicum.request.contract.RequestOperations;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/requests")
public class ParticipationRequestApiController implements RequestOperations {
    private final ParticipationRequestRepository requestRepository;

    @Override
    public List<ParticipationRequestDto> getRequestsByEventId(Long eventId) {
        return requestRepository.findAllByEventId(eventId).stream()
                .map(ParticipationRequestMapper::toDto)
                .toList();
    }

    @Override
    public List<ParticipationRequestDto> getByEventIdAndIdIn(Long eventId, List<Long> requestsIds) {
        return requestRepository.findByEventIdAndIdIn(eventId, requestsIds).stream()
                .map(ParticipationRequestMapper::toDto)
                .toList();
    }

    @Override
    public void updateRequests(List<ParticipationRequestDto> requests) {
        requestRepository.saveAll(requests.stream().map(ParticipationRequestMapper::toRequest).toList());
    }
}
