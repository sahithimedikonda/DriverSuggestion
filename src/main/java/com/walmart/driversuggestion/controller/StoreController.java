package com.walmart.driversuggestion.controller;

import com.walmart.driversuggestion.entity.Store;
import com.walmart.driversuggestion.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/store")
@Slf4j
public class StoreController {

    @Autowired
    StoreService storeService;

    @PostMapping("/")
    public ResponseEntity<Store> saveStore(@RequestBody Store store) {
        log.info("Inside saveStore of StoreController");
        return  new ResponseEntity<>(storeService.saveStore(store), HttpStatus.OK);
    }
}
