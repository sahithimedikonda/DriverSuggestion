package com.walmart.driversuggestion.repository;

import com.walmart.driversuggestion.entity.DriverLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {

    DriverLocation findByDriverID(Long driverID);
}
