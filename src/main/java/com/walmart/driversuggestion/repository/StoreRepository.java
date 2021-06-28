package com.walmart.driversuggestion.repository;

import com.walmart.driversuggestion.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Store findByStoreID(Long storeID);
}
