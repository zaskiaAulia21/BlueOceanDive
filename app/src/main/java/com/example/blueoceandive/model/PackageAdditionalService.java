package com.example.blueoceandive.model;

import java.io.Serializable;

public class PackageAdditionalService implements Serializable {

    private String id;
    private String serviceName;
    private Integer price;

    public PackageAdditionalService() {

    }

    public PackageAdditionalService(String id, String serviceName, Integer price) {
        this.id = id;
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
