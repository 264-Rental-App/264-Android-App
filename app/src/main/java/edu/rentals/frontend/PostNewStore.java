package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class PostNewStore {
    private String name;

    private Double lat;

    private Double lon;

    private String ownerId;

    private String commonAddress;

    private String category;

    private String phoneNumber;

    public PostNewStore(String name, Double lat, Double lon, String ownerId, String address, String category, String phoneNumber) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.ownerId = ownerId;
        this.commonAddress = address;
        this.category = category;
        this.phoneNumber = phoneNumber;
    }

    public String getStoreName() { return name; }

    public Double getLatitude() { return lat; }

    public Double getLongitude() { return lon; }

    public String getStoreId() { return ownerId; }

    public String getStoreAddress() { return commonAddress; }

    public String getStoreCategory() { return category; }

    public String getPhoneNumber() { return phoneNumber; }

}
