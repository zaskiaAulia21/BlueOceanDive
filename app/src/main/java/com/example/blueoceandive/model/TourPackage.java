package com.example.blueoceandive.model;

import java.io.Serializable;

public class TourPackage implements Serializable {

    private String id;
    private String tourPackageName;
    private Integer price;

    public TourPackage() {

    }

    public TourPackage(String id, String tourPackageName, Integer price) {
        this.id = id;
        this.tourPackageName = tourPackageName;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTourPackageName() {
        return tourPackageName;
    }

    public void setTourPackageName(String tourPackageName) {
        this.tourPackageName = tourPackageName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
