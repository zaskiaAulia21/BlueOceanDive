package com.example.blueoceandive.model;

public class TripGallery {

    private String id;
    private String imageUrl;

    public TripGallery() {

    }

    public TripGallery(String id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
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
}
