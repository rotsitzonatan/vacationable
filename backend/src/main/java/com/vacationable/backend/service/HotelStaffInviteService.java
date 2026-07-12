package com.vacationable.backend.service;

import com.vacationable.backend.dto.invite.HotelStaffInviteResponseDto;
import com.vacationable.backend.dto.invite.InviteHotelStaffRequestDto;
import com.vacationable.backend.entity.Hotel;
import com.vacationable.backend.entity.HotelStaffInvite;
import com.vacationable.backend.entity.User;
import com.vacationable.backend.exceptions.ForbiddenException;
import com.vacationable.backend.exceptions.ResourceNotFoundException;
import com.vacationable.backend.repository.HotelRepository;
import com.vacationable.backend.repository.HotelStaffInviteRepository;
import com.vacationable.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class HotelStaffInviteService {

    private static final long INVITE_EXPIRY_HOURS = 48;

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final HotelStaffInviteRepository hotelStaffInviteRepository;

    public HotelStaffInviteService(HotelRepository hotelRepository, UserRepository userRepository,
                                    HotelStaffInviteRepository hotelStaffInviteRepository) {
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
        this.hotelStaffInviteRepository = hotelStaffInviteRepository;
    }

    @Transactional
    public HotelStaffInviteResponseDto createInvite(Integer hotelId, InviteHotelStaffRequestDto request) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        User currentUser = getCurrentUser();
        if (!hotel.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Only the hotel owner can invite staff");
        }

        HotelStaffInvite invite = new HotelStaffInvite();
        invite.setHotel(hotel);
        invite.setEmail(request.getEmail());
        invite.setToken(UUID.randomUUID().toString());
        invite.setExpiresAt(Instant.now().plus(INVITE_EXPIRY_HOURS, ChronoUnit.HOURS));
        invite.setCreatedAt(Instant.now());

        HotelStaffInvite saved = hotelStaffInviteRepository.save(invite);
        return toResponseDto(saved);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private HotelStaffInviteResponseDto toResponseDto(HotelStaffInvite invite) {
        return new HotelStaffInviteResponseDto(
                invite.getId(),
                invite.getHotel().getId(),
                invite.getEmail(),
                invite.getToken(),
                invite.getExpiresAt()
        );
    }
}
