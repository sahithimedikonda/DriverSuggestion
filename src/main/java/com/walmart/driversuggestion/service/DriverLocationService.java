package com.walmart.driversuggestion.service;

import com.walmart.driversuggestion.entity.DriverLocation;
import com.walmart.driversuggestion.entity.Store;
import com.walmart.driversuggestion.repository.DriverLocationRepository;
import com.walmart.driversuggestion.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class DriverLocationService {

    @Autowired
    DriverLocationRepository driverLocationRepository;

    @Autowired
    StoreRepository storeRepository;

    /**
     * Fetch DriverLocation details from DB
     *
     * @return List<DriverLocation>
     */
    public List<DriverLocation> fetchDriverLocations() {
        List<DriverLocation> driverLocations = driverLocationRepository.findAll();
        return driverLocations;
    }

    public DriverLocation save(DriverLocation driverLocation) {
        return driverLocationRepository.save(driverLocation);
    }

    public Map<Long, Double> fetchDrivers(double storeLatitude, double storeLongitude, List<DriverLocation> driverLocations) {
        Map<Long, Double> driverLocationResponses = new LinkedHashMap<>();

        /*double latitude1 = store.getLatitude();
        double longitude1 = store.getLongitude();*/

        Map<Long, Double> unSortedMap = new LinkedHashMap<>();

        for (DriverLocation driverLocation : driverLocations) {

           log.info("Driver Location: " + driverLocation);

            double latitude2Orig = driverLocation.getLatitude();
            double longitude2Orig = driverLocation.getLongitude();

            log.info("Drive latitude2Orig : " + latitude2Orig + " & longitude2Orig : " + longitude2Orig);
            double distance = distance(storeLatitude, latitude2Orig, storeLongitude, longitude2Orig);
            log.info("Distance between co-ordinates in meters: " + distance);
            unSortedMap.put(driverLocation.getDriverID(), distance);
            /* Sort by Distance */
            driverLocationResponses = sortMapLocations(unSortedMap);
        }
        return driverLocationResponses;
    }

    public Store getStoreConfiguration(String storeID){
        //Get Sore configuration using storeID. I have a different microservice for store config which get store details for a given storeID then we use RestTemplate --> restTemplate.getForObject(..,..)
        return storeRepository.findByStoreID(Long.parseLong(storeID));
    }

    /**
     * Sort by Distance
     * @param unSortedMap
     * @return
     */
    Map<Long, Double> sortMapLocations(Map<Long, Double> unSortedMap) {
        log.info("Unsorted Map : " + unSortedMap);

//LinkedHashMap preserve the ordering of elements in which they are inserted
        LinkedHashMap<Long, Double> sortedMap = new LinkedHashMap<>();

        unSortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        log.info("Sorted Map   : " + sortedMap);
        return sortedMap;
    }

    public static double distance(double lat1,
                                  double lat2, double lon1,
                                  double lon2) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in use 3956 for miles
        double r = 3956;

        // calculate the result
        return (c * r);
    }

    public  LinkedHashMap<Long, Double> fetchDriverLocationsByCount(Map<Long, Double> driverLocationSortedResponses, String driverCountStr) {
        int driverCount = Integer.parseInt(driverCountStr);
        log.info("driverCount : "+driverCount);

        LinkedHashMap<Long, Double> nearestDriverLocationsResponse = new LinkedHashMap<>();

        /*if(driverCount < driverLocationSortedResponses.size()) {
            while(driverCount > 0) {
                nearestDriverLocationsResponse.add(driverLocationSortedResponses.keySet().iterator().next());
                driverCount--;
            }
        }*/
        if(driverCount <= driverLocationSortedResponses.size()) {
            for (Map.Entry<Long, Double> entry : driverLocationSortedResponses.entrySet()) //using map.entrySet() for iteration
            {
                //returns keys and values respectively
                if (driverCount > 0) {
                    log.info("Item: " + entry.getKey() + ", Distance: " + entry.getKey());
                    nearestDriverLocationsResponse.put(entry.getKey(), entry.getValue());
                    driverCount--;
                } else {
                    break;
                }
            }
        }
        log.info("nearestDriverLocationsResponse : "+nearestDriverLocationsResponse);
        return nearestDriverLocationsResponse;
    }
}
