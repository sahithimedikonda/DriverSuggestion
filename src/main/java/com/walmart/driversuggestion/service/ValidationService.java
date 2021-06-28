package com.walmart.driversuggestion.service;

import com.walmart.driversuggestion.config.ValidationConfig;
import com.walmart.driversuggestion.entity.DriverLocation;
import com.walmart.driversuggestion.vo.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class contains methods requires to perform validation actions as required by Driver Location app
 */
@Component
@Slf4j
public class ValidationService {

    @Autowired
    private ValidationConfig vc;

    /**
     * Validate DriverLocation object fields and return Error messages object with list of error messages
     * @param location
     * @return ErrorMessages
     */
    public ErrorMessages validateData(DriverLocation location) {
        ErrorMessages errorMessages = new ErrorMessages();
        List<String> errMsgs = errorMessages.getErrors();
        Double latitude = location.getLatitude();
        log.info("Latitude: " + latitude);
        if(latitude == null || latitude < vc.getLatitudeMin() || latitude > vc.getLatitudeMax() ){
            errMsgs.add("Latitude should be between " +  vc.getLatitudeMin() + " and " +  vc.getLatitudeMax());
        }
        Double longitude = location.getLongitude();
        log.info("longitude: " + longitude);
        if(longitude == null || longitude < vc.getLongitudeMin() || longitude > vc.getLongitudeMax() ){
            errMsgs.add("Longitude should be between " +  vc.getLongitudeMin() + " and " +  vc.getLongitudeMax());
        }
        return errorMessages;
    }

    /**
     * Validate latitude, longitude fields and return Error messages object with list of error messages
     * @param latitude
     * @param longitude
     * @return ErrorMessages
     */
    public ErrorMessages validateData(Double latitude, Double longitude) {
        ErrorMessages errorMessages = new ErrorMessages();
        List<String> errMsgs = errorMessages.getErrors();
        log.info("Latitude: " + latitude);
        if(latitude == null || latitude < vc.getLatitudeMin() || latitude > vc.getLatitudeMax() ){
            errMsgs.add("Latitude should be between " +  vc.getLatitudeMin() + " and " +  vc.getLatitudeMax());
        }
        log.info("longitude: " + longitude);
        if(longitude == null || longitude < vc.getLongitudeMin() || longitude > vc.getLongitudeMax() ){
            errMsgs.add("Longitude should be between " +  vc.getLongitudeMin() + " and " +  vc.getLongitudeMax());
        }
        return errorMessages;
    }
}
