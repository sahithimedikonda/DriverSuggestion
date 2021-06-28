package com.walmart.driversuggestion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Store {
    @Id
    private Long storeID;
    private Double latitude;
    private Double longitude;
}
