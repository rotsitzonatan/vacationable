package com.vacationable.backend.service;

import com.vacationable.backend.entity.Hotel;
import com.vacationable.backend.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository){
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> getAllHotels(){
        return hotelRepository.findAll();

    }

    public Optional<Hotel> getHotelById(Integer id){
        return hotelRepository.findById(id);
    }


}
