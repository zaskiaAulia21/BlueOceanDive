package com.example.blueoceandive.model;

import com.example.blueoceandive.util.NumberFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TripPackage implements Serializable {

    private String id;
    private String imageUrl;
    private String packageName;
    private Integer packageDuration;
    private String frontDescription;
    private Integer totalReview;
    private Integer price;
    private String priceDescription;
    private Float rating;
    private String packageDescription;
    private String location;
    private String include;
    private String note;
    private ArrayList<TourPackage> tourPackages;
    private ArrayList<PackageAdditionalService> additionalShuttleService;

    public TripPackage() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getPackageDuration() {
        return packageDuration;
    }

    public String getFormattedDuration() {
        return String.format(Locale.getDefault(), "%d Days", getPackageDuration());
    }

    public void setPackageDuration(Integer packageDuration) {
        this.packageDuration = packageDuration;
    }

    public String getFrontDescription() {
        return frontDescription;
    }

    public void setFrontDescription(String frontDescription) {
        this.frontDescription = frontDescription;
    }

    public Integer getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(Integer totalReview) {
        this.totalReview = totalReview;
    }

    public Integer getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return NumberFormat.format(getPrice());
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<TourPackage> getTourPackages() {
        return tourPackages;
    }

    public void setTourPackages(ArrayList<TourPackage> tourPackages) {
        this.tourPackages = tourPackages;
    }

    public ArrayList<PackageAdditionalService> getAdditionalShuttleService() {
        return additionalShuttleService;
    }

    public void setAdditionalShuttleService(ArrayList<PackageAdditionalService> additionalShuttleService) {
        this.additionalShuttleService = additionalShuttleService;
    }
}
