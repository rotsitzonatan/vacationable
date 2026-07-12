package com.vacationable.backend.service;

import com.vacationable.backend.dto.hotel.CreateHotelRequestDto;
import com.vacationable.backend.dto.hotel.HotelResponseDto;
import com.vacationable.backend.dto.hotel.UpdateHotelRequestDto;
import com.vacationable.backend.dto.location.LocationResponseDto;
import com.vacationable.backend.entity.Hotel;
import com.vacationable.backend.entity.Location;
import com.vacationable.backend.entity.User;
import com.vacationable.backend.exceptions.ResourceNotFoundException;
import com.vacationable.backend.repository.HotelRepository;
import com.vacationable.backend.repository.LocationRepository;
import com.vacationable.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public HotelService(HotelRepository hotelRepository, LocationRepository locationRepository,
                         UserRepository userRepository) {
        this.hotelRepository = hotelRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
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
        User owner = getCurrentUser();

        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setAddress(request.getAddress());
        hotel.setLocation(location);
        hotel.setOwner(owner);
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

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
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
                locationDto,
                hotel.getOwner().getId()
        );
    }
}
