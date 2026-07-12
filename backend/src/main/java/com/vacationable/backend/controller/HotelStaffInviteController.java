package com.vacationable.backend.controller;

import com.vacationable.backend.dto.invite.HotelStaffInviteResponseDto;
import com.vacationable.backend.dto.invite.InviteHotelStaffRequestDto;
import com.vacationable.backend.service.HotelStaffInviteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels/{hotelId}/invites")
public class HotelStaffInviteController {

    private final HotelStaffInviteService hotelStaffInviteService;

    public HotelStaffInviteController(HotelStaffInviteService hotelStaffInviteService) {
        this.hotelStaffInviteService = hotelStaffInviteService;
    }

    @PostMapping
    public ResponseEntity<HotelStaffInviteResponseDto> createInvite(@PathVariable Integer hotelId,
                                                                      @Valid @RequestBody InviteHotelStaffRequestDto request) {
        HotelStaffInviteResponseDto invite = hotelStaffInviteService.createInvite(hotelId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(invite);
    }
}
