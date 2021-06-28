package com.walmart.driversuggestion.vo;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores Error message details
 */
public class ErrorMessages {

    List<String> errors = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString(){
        return "\"" + StringUtils.join(errors, "\",\"") + "\"";
    }
}
