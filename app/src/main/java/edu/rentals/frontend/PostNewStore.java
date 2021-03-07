package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class PostNewStore {
    private String storeName;

    private Double storeLat;

    private Double storeLong;

    private String ownerId;

    private String storeAddress;

    private String storeCategory;

    private String phoneNumber;

    public PostNewStore(String name, Double lat, Double lon, String ownerId, String address, String category, String phoneNumber) {
        this.storeName = name;
        this.storeLat = lat;
        this.storeLong = lon;
        this.ownerId = ownerId;
        this.storeAddress = address;
        this.storeCategory = category;
        this.phoneNumber = phoneNumber;
    }

    public String getStoreName() { return storeName; }

    public Double getLatitude() { return storeLat; }

    public Double getLongitude() { return storeLong; }

    public String getStoreId() { return ownerId; }

    public String getStoreAddress() { return storeAddress; }

    public String getStoreCategory() { return storeCategory; }

    public String getPhoneNumber() { return phoneNumber; }

}
