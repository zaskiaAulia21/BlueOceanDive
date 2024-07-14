package com.example.blueoceandive.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckoutItem implements Serializable {

    private String fullName;
    private String region;
    private String streetAddress;
    private String postCode;
    private String city;
    private String phone;
    private String email;
    private ArrayList<CartItem> carts;

    public CheckoutItem() {

    }

    public CheckoutItem(String fullName, String region, String streetAddress, String postCode, String city, String phone, String email, ArrayList<CartItem> carts) {
        this.fullName = fullName;
        this.region = region;
        this.streetAddress = streetAddress;
        this.postCode = postCode;
        this.city = city;
        this.phone = phone;
        this.email = email;
        this.carts = carts;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<CartItem> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<CartItem> carts) {
        this.carts = carts;
    }
}
