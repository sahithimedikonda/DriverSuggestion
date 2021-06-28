package com.walmart.driversuggestion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.driversuggestion.entity.DriverLocation;
import com.walmart.driversuggestion.entity.Store;
import com.walmart.driversuggestion.producer.KafkaDriverLocationProducer;
import com.walmart.driversuggestion.service.DriverLocationService;
import com.walmart.driversuggestion.service.ValidationService;
import com.walmart.driversuggestion.vo.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static com.walmart.driversuggestion.constants.RequestResponseConstants.RequestResponseConstants.RESPONSE_ENTITY_NOT_FOUND;

@Controller
@Slf4j
public class KafkaDriverLocationController {

    @Autowired
    KafkaDriverLocationProducer producer;

    @Autowired
    DriverLocationService driverLocationService;

    @Autowired
    ValidationService validateService;

    @PostMapping("/publish")
    public ResponseEntity<String> writeMessageToTopic(@RequestBody String driverLocationStr) {
        log.info("KafkaDriverLocationController message : " + driverLocationStr);
        /* Validate for invalid data and return errors as JSON */
        DriverLocation driverLocation;
        try {
            driverLocation = new ObjectMapper().readValue(driverLocationStr, DriverLocation.class);
        } catch (IOException e) {
            log.error("Error converting Location status JSON to Object: " + e.getMessage());
            return new ResponseEntity<>("{\"errors\": [\"Invalid fields in body: Use Latitude,Longitude and DriverID\"]}", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ErrorMessages errorMessages = validateService.validateData(driverLocation);
        List<String> errMsgs = errorMessages.getErrors();
        log.info("Error messages: " + errMsgs);
        ResponseEntity responseEntity;
        if (errMsgs.isEmpty()) {
            /* send the location details to Kafka topic*/
            this.producer.writeDriverLocationMessage(driverLocationStr);
            responseEntity =  new ResponseEntity<>("Message published to topic successfully!!!!", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(errorMessages, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return responseEntity;
    }

    @GetMapping("/drivers")
    public ResponseEntity<LinkedHashMap<Long, Double>> getNearestDrivers(@RequestParam(value = "storeID") String storeIdStr, @RequestParam(value = "driverCount") String driverCount) {
        log.info("Request to /drivers with input: storeID=" + storeIdStr);

        /* Validate for invalid data and return errors as JSON */
        //error validations goes here....
        /* Verify if ID is integer */
        if (!StringUtils.isNumeric(storeIdStr)) {
            return RESPONSE_ENTITY_NOT_FOUND;
        }

        Store store = driverLocationService.getStoreConfiguration(storeIdStr);

        /* Validate for invalid data and return errors as JSON */
        ErrorMessages errorMessages = validateService.validateData(store.getLatitude(), store.getLongitude());
        List<String> errMsgs = errorMessages.getErrors();
        log.info("Error messages: " + errMsgs);
        ResponseEntity responseEntity;
        if (errMsgs.isEmpty()) {
        /* Fetch all applicable Driver Locations */
        List<DriverLocation> driverLocations = driverLocationService.fetchDriverLocations();
        log.info("Driver Locations: " + driverLocations);

        /* Fetch sorted driver locations */
        Map<Long, Double> driverLocationSortedResponses = driverLocationService.fetchDrivers(store.getLatitude(), store.getLongitude(), driverLocations);
        log.info("Driver Locations Responses: " + driverLocationSortedResponses);

        LinkedHashMap<Long, Double> nearestDriverLocationsResponse = driverLocationService.fetchDriverLocationsByCount(driverLocationSortedResponses, driverCount);
        log.info("Controller nearestDriverLocationsResponse : "+nearestDriverLocationsResponse);

        responseEntity = new ResponseEntity<>(nearestDriverLocationsResponse, HttpStatus.OK);
       } else {
            responseEntity = new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

}
