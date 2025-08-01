package ru.practicum.location.model;

import lombok.experimental.UtilityClass;
import ru.practicum.location.dto.LocationDto;

@UtilityClass
public class MapperLocation {
    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getId(), location.getLat(), location.getLon());
    }

    public static Location toLocation(LocationDto locationDto) {
        if(locationDto == null) { return null; }
        return Location.builder()
                .id(locationDto.id())
                .lat(locationDto.lat())
                .lon(locationDto.lon())
                .build();
    }
}
