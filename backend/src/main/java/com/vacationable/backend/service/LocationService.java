package com.vacationable.backend.service;

import com.vacationable.backend.dto.location.LocationRequestDto;
import com.vacationable.backend.dto.location.LocationResponseDto;
import com.vacationable.backend.entity.Hotel;
import com.vacationable.backend.entity.Location;
import com.vacationable.backend.exceptions.ResourceNotFoundException;
import com.vacationable.backend.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional(readOnly = true)
    public List<LocationResponseDto> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocationResponseDto getLocationById(Integer id) {
        Location location = findLocationOrThrow(id);
        return toResponseDto(location);
    }

    @Transactional
    public LocationResponseDto createLocation(LocationRequestDto request) {
        Location location = new Location();

        location.setCityName(request.getCityName());
        location.setCountry(request.getCountry());
        location.setCreatedAt(Instant.now());

        Location saved = locationRepository.save(location);
        return toResponseDto(saved);
    }

    @Transactional
    public LocationResponseDto updateLocation(Integer id, LocationRequestDto request) {

        Location location = findLocationOrThrow(id);

        location.setCityName(request.getCityName());
        location.setCountry(request.getCountry());
        location.setCreatedAt(Instant.now());

        Location saved = locationRepository.save(location);
        return toResponseDto(saved);
    }

    @Transactional
    public void deleteLocation(Integer id) {
        Location location = findLocationOrThrow(id);
        locationRepository.delete(location);
    }

    private Location findLocationOrThrow(Integer id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
    }

    private LocationResponseDto toResponseDto(Location location) {
        return new LocationResponseDto(
                location.getId(),
                location.getCityName(),
                location.getCountry()
        );
    }
}
