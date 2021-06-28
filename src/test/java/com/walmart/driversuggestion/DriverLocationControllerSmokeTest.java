package com.walmart.driversuggestion;

import com.walmart.driversuggestion.controller.KafkaDriverLocationController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class DriverLocationControllerSmokeTest {
    @Autowired
    KafkaDriverLocationController driverLocationController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(driverLocationController).isNotNull();
    }
}
