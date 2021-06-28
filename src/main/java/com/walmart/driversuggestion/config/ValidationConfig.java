package com.walmart.driversuggestion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * This class loads validation related properties (validation.XXX) from files in the given order
 */
@Configuration
@PropertySources({
        @PropertySource("classpath:env.properties")
})
public class ValidationConfig {

    @Value("${validation.driverIdMin:1}")
    private int driverIdMin;
    @Value("${validation.driverIdMax:50000}")
    private int driverIdMax;

    @Value("${validation.latitudeMin:-90}")
    private double latitudeMin;
    @Value("${validation.latitudeMax:90}")
    private double latitudeMax;

    @Value("${validation.longitudeMin:-180}")
    private double longitudeMin;
    @Value("${validation.longitudeMax:180}")
    private double longitudeMax;

    public int getDriverIdMin() {
        return driverIdMin;
    }

    public int getDriverIdMax() {
        return driverIdMax;
    }

    public double getLatitudeMin() {
        return latitudeMin;
    }

    public double getLatitudeMax() {
        return latitudeMax;
    }

    public double getLongitudeMin() {
        return longitudeMin;
    }

    public double getLongitudeMax() {
        return longitudeMax;
    }

    /* @Bean on method indicates that this method will return an object that
     * should be registered as a bean in the Spring application context.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer p =  new PropertySourcesPlaceholderConfigurer();
        p.setIgnoreResourceNotFound(true);

        return p;
    }
}
