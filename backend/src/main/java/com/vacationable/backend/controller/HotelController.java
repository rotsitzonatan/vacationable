package com.vacationable.backend.controller;

import com.vacationable.backend.entity.Hotel;
import com.vacationable.backend.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<Hotel>>getAllHotels(){

        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Integer id) {
        return hotelService.getHotelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
