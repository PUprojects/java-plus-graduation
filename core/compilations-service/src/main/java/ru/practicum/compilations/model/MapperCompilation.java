package ru.practicum.compilations.model;

import lombok.RequiredArgsConstructor;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@RequiredArgsConstructor
public class MapperCompilation {

    public static CompilationsDto toDto(Compilation compilation, List<EventShortDto> eventShortDtos) {
        return CompilationsDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(eventShortDtos)
                .build();
    }
}
