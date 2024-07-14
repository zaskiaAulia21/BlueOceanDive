package com.example.blueoceandive.model;

public class ProcessItem {

    private String packageName;
    private String packageImageUrl;

    public ProcessItem(String packageName, String packageImageUrl) {
        this.packageName = packageName;
        this.packageImageUrl = packageImageUrl;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageImageUrl() {
        return packageImageUrl;
    }

    public void setPackageImageUrl(String packageImageUrl) {
        this.packageImageUrl = packageImageUrl;
    }
}
