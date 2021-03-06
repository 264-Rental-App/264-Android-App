package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

public class Store {

    @SerializedName("name")
    private String storeName;
    @SerializedName("lat")
    private Double storeLat;
    @SerializedName("long")
    private Double storeLong;
    @SerializedName("ownerId")
    private String ownerId;
    @SerializedName("commonAddress")
    private String storeAddress;
    @SerializedName("category")
    private String storeCategory;

    public Store(String name, Double lat, Double lon, String ownerId, String address, String category) {
        this.storeName = name;
        this.storeLat = lat;
        this.storeLong = lon;
        this.ownerId = ownerId;
        this.storeAddress = address;
        this.storeCategory = category;
    }

    public String getStoreName() { return storeName; }

    public Double getLatitude() { return storeLat; }

    public Double getLongitude() { return storeLong; }

    public String getStoreId() { return ownerId; }

    public String getStoreAddress() { return storeAddress; }

    public String getStoreCategory() { return storeCategory; }

}
