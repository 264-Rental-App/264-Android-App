package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Store {

    @SerializedName("name")
    private String storeName;
    @SerializedName("lat")
    private Double storeLat;
    @SerializedName("lon")
    private Double storeLong;
    @SerializedName("id")
    private Long id;
    @SerializedName("ownerId")
    private String ownerId;
    @SerializedName("commonAddress")
    private String storeAddress;
    @SerializedName("category")
    private String storeCategory;
    @SerializedName("phoneNumber")
    private String phoneNumber;

    public Store(String name, Double lat, Double lon, Long id, String ownerId, String address, String category, String phoneNumber) {
        this.storeName = name;
        this.storeLat = lat;
        this.storeLong = lon;
        this.id = id;
        this.ownerId = ownerId;
        this.storeAddress = address;
        this.storeCategory = category;
        this.phoneNumber = phoneNumber;
    }

    public String getStoreName() { return storeName; }

    public Double getLatitude() { return storeLat; }

    public Double getLongitude() { return storeLong; }

    public Long getId() { return id; }

    public String getStoreId() { return ownerId; }

    public String getStoreAddress() { return storeAddress; }

    public String getStoreCategory() { return storeCategory; }

    public String getPhoneNumber() { return phoneNumber; }

}
