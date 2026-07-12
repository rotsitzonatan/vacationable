package com.vacationable.backend.repository;

import com.vacationable.backend.entity.HotelStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelStaffRepository extends JpaRepository<HotelStaff, Integer> {

    Optional<HotelStaff> findByHotelIdAndUserId(Integer hotelId, Integer userId);
}
