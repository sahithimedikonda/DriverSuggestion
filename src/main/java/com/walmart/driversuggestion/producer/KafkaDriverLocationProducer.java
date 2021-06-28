package com.walmart.driversuggestion.producer;

import com.walmart.driversuggestion.entity.DriverLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaDriverLocationProducer {

    private static final String TOPIC = "driver_location";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void writeDriverLocationMessage(String driverLocationMsg) {
log.info("driverLocationMsg : "+driverLocationMsg);
        this.kafkaTemplate.send(TOPIC, driverLocationMsg);
    }
}
