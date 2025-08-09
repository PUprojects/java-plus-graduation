package ru.practicum.compilations.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.CompilationsPublicParam;
import ru.practicum.compilations.dto.RequestCompilationCreate;
import ru.practicum.compilations.dto.RequestCompilationUpdate;
import ru.practicum.compilations.event.feign.EventClient;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.model.MapperCompilation;
import ru.practicum.compilations.model.QCompilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.event.dto.EventShortDto;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventClient eventClient;

    @PersistenceUnit
    private EntityManagerFactory entityManager;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void initQueryFactory() {
        queryFactory = new JPAQueryFactory(entityManager.createEntityManager());
    }

    @Override
    @Transactional
    public CompilationsDto create(RequestCompilationCreate create) {
        List<EventShortDto> eventShortDtos;
        Compilation compilation = new Compilation();
        compilation.setTitle(create.getTitle());
        compilation.setPinned(Optional.ofNullable(create.getPinned()).orElse(false));
        if (create.getEvents() != null) {
            eventShortDtos = eventClient.getAllByIdsList(create.getEvents());

        } else {
            eventShortDtos = new ArrayList<>();

        }
        compilation.setEventsIds(eventShortDtos.stream()
                .map(EventShortDto::getId)
                .toList());

        return MapperCompilation.toDto(compilationRepository.save(compilation), eventShortDtos);
    }

    @Override
    @Transactional
    public CompilationsDto update(long compId, RequestCompilationUpdate update) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Подборка не найдена"));
        if (Objects.nonNull(update.getPinned())) {
            compilation.setPinned(update.getPinned());
        }
        if (Objects.nonNull(update.getTitle())) {
            compilation.setTitle(update.getTitle());
        }

        List<EventShortDto> eventShortDtos;

        if (Objects.nonNull(update.getEvents())) {
            eventShortDtos = eventClient.getAllByIdsList(update.getEvents());
            compilation.setEventsIds(eventShortDtos.stream()
                    .map(EventShortDto::getId)
                    .toList());
        } else {
            eventShortDtos = eventClient.getAllByIdsList(compilation.getEventsIds());
        }

        return MapperCompilation.toDto(compilationRepository.save(compilation), eventShortDtos);
    }

    @Override
    @Transactional
    public void delete(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationsDto getById(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(EntityNotFoundException::new);
        List<EventShortDto> eventShortDtos = eventClient.getAllByIdsList(compilation.getEventsIds());
        return MapperCompilation.toDto(compilation, eventShortDtos);
    }

    @Override
    public Collection<CompilationsDto> findCompilations(CompilationsPublicParam param) {
        QCompilation compilation = QCompilation.compilation;
        JPAQuery<Compilation> query = queryFactory.selectFrom(compilation)
                .offset(param.from())
                .limit(param.size());

        if (Objects.nonNull(param.pinned())) {
            BooleanExpression expression = compilation.pinned.eq(param.pinned());
            query.where(expression);
        }

        Collection<Compilation> compilations = query.fetch();

        return compilations.stream()
                .map(current -> {
                    List<EventShortDto> eventShortDtos = eventClient.getAllByIdsList(current.getEventsIds());
                    return MapperCompilation.toDto(current, eventShortDtos);
                })
                .toList();
    }
}
