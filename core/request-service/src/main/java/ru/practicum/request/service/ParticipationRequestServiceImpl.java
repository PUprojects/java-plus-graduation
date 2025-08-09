package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.request.event.feign.EventClient;
import ru.practicum.request.user.feign.UserClient;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.enums.EventState;
import ru.practicum.exception_handler.ConflictException;
import ru.practicum.exception_handler.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.request.mapper.ParticipationRequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.repository.ParticipationRequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventClient eventClient;
    private final UserClient userClient;

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        checkUserExist(userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        checkUserExist(userId);
        EventFullDto event = eventClient.getEventById(eventId);

        if (requestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent()) {
            throw new ConflictException("Request already exists");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new IllegalStateException("Нельзя подавать заявку на своё собственное событие.");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new IllegalStateException("Нельзя подавать заявку на неопубликованное событие.");
        }


        List<ParticipationRequest> requests = requestRepository.findAllByEventId(event.getId());

        if (!event.getRequestModeration() && requests.size() >= event.getParticipantLimit()) {
            throw new ConflictException("Member limit exceeded ");
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .requesterId(userId)
                .eventId(event.getId())
                .status(RequestStatus.PENDING)
                .created(LocalDateTime.now())
                .build();
        if (event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        return ParticipationRequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Заявка не найдена."));

        if (!request.getRequesterId().equals(userId)) {
            throw new IllegalStateException("Вы не можете отменить чужую заявку.");
        }

        request.setStatus(RequestStatus.CANCELED);
        return ParticipationRequestMapper.toDto(request);
    }

    private void throwDuplicateRequest() {
        throw new IllegalStateException("Заявка уже есть.");
    }

    private void checkUserExist(Long userId) {
        if(!userClient.isUserExist(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    private EventFullDto getEventOrThrow(Long eventId) {
        EventFullDto eventFullDto = eventClient.getEventById(eventId);
        if(eventFullDto == null) {
            throw new NotFoundException("Событие не найдено.");
        }

        return eventFullDto;
//        return eventRepository.findById(eventId)
//                .orElseThrow(() -> new NotFoundException("Событие не найдено."));
    }
}
