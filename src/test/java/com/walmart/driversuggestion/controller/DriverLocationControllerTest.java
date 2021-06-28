package com.walmart.driversuggestion.controller;

import com.walmart.driversuggestion.config.ValidationConfig;
import com.walmart.driversuggestion.entity.DriverLocation;
import com.walmart.driversuggestion.repository.DriverLocationRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Testing class for Driver Location Controller
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class DriverLocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ValidationConfig vc;

    @Autowired
    private DriverLocationRepository driverLocationRepository;

    private static String ERR_LATITUDE, ERR_LONGITUDE, ERR_ACCURACY;
    private static boolean INIT_IS_DONE = false;

    //@Before
    public void init() throws Exception {
        if (INIT_IS_DONE) { // To DO the initialization only once for all testes
            return;
        }
        // do the initialization
        ERR_LATITUDE = "Latitude should be between " + vc.getLatitudeMin() + " and " + vc.getLatitudeMax();
        ERR_LONGITUDE = "Longitude should be between " + vc.getLongitudeMin() + " and " + vc.getLongitudeMax();

        /* Setup DB */
        //driverLocationRepository.deleteAll();

        double[] latitudes = {12.987654, 12.965687, 12.986568, 12.995657, 12.985659, 12.995651, 12.996553, 12.995655, 11.954655, 12.576665, 12.645437, 12.743437, 12.998677, 12.998687, 12.998697, 12.998607, 12.998657, 12.998677, 12.998677, 12.998697};
        double[] longitudes = {77.123456, 77.245455, 77.254555, 77.345865, 77.345555, 77.340436, 77.634835, 78.344441, 78.323532, 76.554867, 76.155455, 75.565645, 77.423464, 77.423464, 77.423464, 77.423454, 77.423474, 77.423564, 77.423664, 77.423474};

        /* Set the time to N min minus current time where N is length of data */
        LocalDateTime nowMinusNmin = LocalDateTime.now().minus(latitudes.length, ChronoUnit.MINUTES);
        /* Insert each row and increment time by 1 min */
        for (int i = 0; i < latitudes.length; i++) {
            driverLocationRepository.save(new DriverLocation(new Long((i + 1)), latitudes[i], longitudes[i]));
        }

        driverLocationRepository.findAll().forEach(System.out::println);

        INIT_IS_DONE = true;
    }

    @After
    public void destroy() throws Exception {
        //driverLocationRepository.deleteAll();
    }

    @Test
    public void setDriverLocation() throws Exception {
        int id = (int) (Math.random() + 1) * 100;
        double latitude = 12 + Math.random();
        double longitude = 77 + Math.random();
        double accuracy = Math.random();

        String body = String.format("{\"latitude\": %f,\"longitude\": %f}", latitude, longitude);
        System.out.println("PUT Body for Id: " + id + " | " + body);
        mockMvc.perform(post("/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Message published to topic successfully!!!!")));
    }

    @Test
    public void setDriverLocationUserIdNotInteger() throws Exception {
        String id = "abc";
        double latitude = 12 + Math.random();
        double longitude = 77 + Math.random();

        String body = String.format("{\"latitude\": %f,\"longitude\": %f}", latitude, longitude);
        System.out.println("PUT Body for Id: " + id + " | " + body);
        mockMvc.perform(post("/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void setDriverLocationUserIdOutOfRange() throws Exception {
        int id = 50000000;
        double latitude = 12 + Math.random();
        double longitude = 77 + Math.random();

        String body = String.format("{\"latitude\": %f,\"longitude\": %f}", latitude, longitude);
        System.out.println("PUT Body for Id: " + id + " | " + body);
        mockMvc.perform(post("/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void setDriverLocationBodyNotPresent() throws Exception {
        int id = (int) (Math.random() + 1) * 100;

        String body = null;
        System.out.println("PUT Body for Id: " + id + " | " + body);
        mockMvc.perform(post("/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void setDriverLocationDataKeysNotPresent() throws Exception {
        int id = (int) (Math.random() + 1) * 100;

        String body = "{}";
        System.out.println("PUT Body for Id: " + id + " | " + body);
        mockMvc.perform(post("/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void setDriverLocationLatitudeIsOutOfRange() throws Exception {
        int id = (int) (Math.random() + 1) * 100;
        double latitude = 112 + Math.random();
        double longitude = 77 + Math.random();

        String body = String.format("{\"latitude\": %f,\"longitude\": %f}", latitude, longitude);

        validatePOSTTErrorRequest(id, body, "{\"errors\":[\"" + ERR_LATITUDE + "\"]}");

    }

    @Test
    public void setDriverLocationLongitudeIsOutOfRange() throws Exception {
        int id = (int) (Math.random() + 1) * 100;
        double latitude = 12 + Math.random();
        double longitude = 277 + Math.random();

        String body = String.format("{\"latitude\": %f,\"longitude\": %f}", latitude, longitude);

        validatePOSTTErrorRequest(id, body, "{\"errors\":[\"" + ERR_LONGITUDE + "\"]}");
    }

    @Test
    public void setDriverLocationAccuracyIsOutOfRange() throws Exception {
        int id = (int) (Math.random() + 1) * 100;
        double latitude = 12 + Math.random();
        double longitude = 77 + Math.random();

        String body = String.format("{\"latitude\": %f,\"longitude\": %f}", latitude, longitude);

        validatePOSTTErrorRequest(id, body, "{\"errors\":[\"" + ERR_ACCURACY + "\"]}");
    }

    @Test
    public void setDriverLocationLatitudeLongitudeIsOutOfRange() throws Exception {
        int id = (int) (Math.random() + 1) * 100;
        double latitude = 112 + Math.random();
        double longitude = 277 + Math.random();

        String body = String.format("{\"latitude\": %f,\"longitude\": %f}", latitude, longitude);

        validatePOSTTErrorRequest(id, body, "{\"errors\":[\"" + ERR_LATITUDE + "\",\"" + ERR_LONGITUDE + "\"]}");
    }

    @Test
    public void setDriverLocationLatitudeLongitudeAccuracyIsOutOfRange() throws Exception {
        int id = (int) (Math.random() + 1) * 100;
        double latitude = 112 + Math.random();
        double longitude = 277 + Math.random();

        String body = String.format("{\"latitude\": %f,\"longitude\": %f}", latitude, longitude);

        validatePOSTTErrorRequest(id, body, "{\"errors\":[\"" + ERR_LATITUDE + "\",\"" + ERR_LONGITUDE + "\"]}");
    }


    public void validatePOSTTErrorRequest(int id, String body, String content) throws Exception {
        System.out.println("PUT Body for Id: " + id + " | " + body);
        System.out.println("Content: " + content);
        mockMvc.perform(post("/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(equalTo(content))); // TODO Replace equalTO with contains and check for each expected error separately
    }

    @Test
    public void getDriversAllValuesSet() throws Exception {
        mockMvc.perform(get("/drivers?storeID=129&driverCount=3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"12\":4208.970457124269,\"11\":4249.228500559876,\"10\":4276.516982180285}")));
    }

    @Test
    public void getDriversLatitudeOutOfRange() throws Exception {
        mockMvc.perform(get("/drivers?storeID=129sq&driverCount=3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{}")));
    }

    @Test
    public void getDriversStoreIdNotPresent() throws Exception {
        mockMvc.perform(get("/drivers?driverCount=3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}