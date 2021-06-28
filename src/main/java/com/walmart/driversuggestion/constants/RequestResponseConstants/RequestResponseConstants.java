package com.walmart.driversuggestion.constants.RequestResponseConstants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class RequestResponseConstants {

    public static final ResponseEntity RESPONSE_ENTITY_NOT_FOUND = new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
    public static final ResponseEntity RESPONSE_ENTITY_OK = new ResponseEntity<>("{}",HttpStatus.OK);

}