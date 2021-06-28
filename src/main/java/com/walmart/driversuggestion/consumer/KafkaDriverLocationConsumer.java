package com.walmart.driversuggestion.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.driversuggestion.entity.DriverLocation;
import com.walmart.driversuggestion.repository.DriverLocationRepository;
import com.walmart.driversuggestion.service.DriverLocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class KafkaDriverLocationConsumer {

    @Autowired
    DriverLocationService driverLocationService;

    @KafkaListener(topics="driver_location", groupId = "driver_group_id")
    public ResponseEntity getMessage(String locationStatus){
        System.out.println("KafkaDriverLocationConsumer locationStatus received: "+ locationStatus);
        //JSON from String to Object
        ObjectMapper readMapper = new ObjectMapper();
        DriverLocation driverLocation;
        ResponseEntity responseEntity;
        try {
            driverLocation = readMapper.readValue(locationStatus, DriverLocation.class);
            driverLocationService.save(driverLocation);
        } catch (IOException e) {
            log.error("Error converting Location status JSON to Object: " + e.getMessage());
            return new ResponseEntity<>("{\"errors\": [\"Invalid fields in body: Use Latitude,Longitude and DriverID\"]}", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>("{\"status\": [\"Success\"]}", HttpStatus.OK);
    }
}
