package com.vacationable.backend.repository;

import com.vacationable.backend.entity.HotelStaffPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelStaffPermissionRepository extends JpaRepository<HotelStaffPermission, Integer> {
}
