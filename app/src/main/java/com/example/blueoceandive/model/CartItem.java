package com.example.blueoceandive.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String cartId;

    private TripPackage tripPackage;
    private String pickupDate;
    private TourPackage tourPackage;
    private PackageAdditionalService packageAdditionalService;
    private Long subtotal;
    private Boolean isChecked;

    public CartItem() {

    }

    public CartItem(String cartId, TripPackage tripPackage, String pickupDate, TourPackage tourPackage, PackageAdditionalService packageAdditionalService, Long subtotal, Boolean isChecked) {
        this.cartId = cartId;
        this.tripPackage = tripPackage;
        this.pickupDate = pickupDate;
        this.tourPackage = tourPackage;
        this.packageAdditionalService = packageAdditionalService;
        this.subtotal = subtotal;
        this.isChecked = isChecked;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public TripPackage getTripPackage() {
        return tripPackage;
    }

    public void setTripPackage(TripPackage tripPackage) {
        this.tripPackage = tripPackage;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public TourPackage getTourPackage() {
        return tourPackage;
    }

    public void setTourPackage(TourPackage tourPackage) {
        this.tourPackage = tourPackage;
    }

    public PackageAdditionalService getPackageAdditionalService() {
        return packageAdditionalService;
    }

    public void setPackageAdditionalService(PackageAdditionalService packageAdditionalService) {
        this.packageAdditionalService = packageAdditionalService;
    }

    public Long getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Long subtotal) {
        this.subtotal = subtotal;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
