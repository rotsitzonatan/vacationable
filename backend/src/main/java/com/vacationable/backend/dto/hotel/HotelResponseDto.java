package com.vacationable.backend.dto.hotel;

import com.vacationable.backend.dto.location.LocationResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDto {

    private Integer id;

    private String name;

    private String description;

    private String address;

    private Float rating;

    private Integer totalReviews;

    private LocationResponseDto location;
}