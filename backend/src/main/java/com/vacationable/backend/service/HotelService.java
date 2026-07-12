package com.vacationable.backend.service;

import com.vacationable.backend.dto.hotel.CreateHotelRequestDto;
import com.vacationable.backend.dto.hotel.HotelResponseDto;
import com.vacationable.backend.dto.hotel.UpdateHotelRequestDto;
import com.vacationable.backend.dto.location.LocationResponseDto;
import com.vacationable.backend.entity.Hotel;
import com.vacationable.backend.entity.Location;
import com.vacationable.backend.exceptions.ResourceNotFoundException;
import com.vacationable.backend.repository.HotelRepository;
import com.vacationable.backend.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final LocationRepository locationRepository;

    public HotelService(HotelRepository hotelRepository, LocationRepository locationRepository) {
        this.hotelRepository = hotelRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional(readOnly = true)
    public List<HotelResponseDto> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public HotelResponseDto getHotelById(Integer id) {
        Hotel hotel = findHotelOrThrow(id);
        return toResponseDto(hotel);
    }

    @Transactional
    public HotelResponseDto createHotel(CreateHotelRequestDto request) {
        Location location = findLocationOrThrow(request.getLocationId());

        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setAddress(request.getAddress());
        hotel.setLocation(location);
        hotel.setCreatedAt(Instant.now());
        hotel.setUpdatedAt(Instant.now());

        Hotel saved = hotelRepository.save(hotel);
        return toResponseDto(saved);
    }

    @Transactional
    public HotelResponseDto updateHotel(Integer id, UpdateHotelRequestDto request) {
        Hotel hotel = findHotelOrThrow(id);
        Location location = findLocationOrThrow(request.getLocationId());

        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setAddress(request.getAddress());
        hotel.setLocation(location);
        hotel.setUpdatedAt(Instant.now());

        Hotel saved = hotelRepository.save(hotel);
        return toResponseDto(saved);
    }

    @Transactional
    public void deleteHotel(Integer id) {
        Hotel hotel = findHotelOrThrow(id);
        hotelRepository.delete(hotel);
    }

    private Hotel findHotelOrThrow(Integer id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
    }

    private Location findLocationOrThrow(Integer id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
    }

    private HotelResponseDto toResponseDto(Hotel hotel) {
        LocationResponseDto locationDto = new LocationResponseDto(
                hotel.getLocation().getId(),
                hotel.getLocation().getCityName(),
                hotel.getLocation().getCountry()
        );

        return new HotelResponseDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getAddress(),
                hotel.getRating() != null ? hotel.getRating().floatValue() : null,
                hotel.getTotalReviews(),
                locationDto
        );
    }
}
