package ru.practicum.location.dto;

import jakarta.persistence.Column;

public record LocationDto(Long id, Float lat, Float lon) {
}
