package com.vacationable.backend.repository;

import com.vacationable.backend.entity.HotelStaffInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelStaffInviteRepository extends JpaRepository<HotelStaffInvite, Integer> {

    Optional<HotelStaffInvite> findByToken(String token);
}
