package com.vacationable.backend.dto.invite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelStaffInviteResponseDto {

    private Integer id;

    private Integer hotelId;

    private String email;

    private String token;

    private Instant expiresAt;
}
