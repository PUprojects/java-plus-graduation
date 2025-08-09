package ru.practicum.compilations.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class RequestCompilationUpdate {
    List<Long> events;
    Boolean pinned;
    @Size(min = 1, max = 50)
    String title;
}
