package com.walmart.driversuggestion.service;

import com.walmart.driversuggestion.entity.Store;
import com.walmart.driversuggestion.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StoreService {

    @Autowired
    StoreRepository storeRepository;

    public Store saveStore(Store store) {
        log.info("Inside saveStoreConfiguration of StoreService storeRepository : "+storeRepository);
        return storeRepository.save(store);
    }
}
