package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

public class Store {

    @SerializedName("name")
    private String storeName;
    @SerializedName("lat")
    private Float storeLat;
    @SerializedName("long")
    private Float storeLong;
    @SerializedName("id")
    private int storeId;
    @SerializedName("commonAddress")
    private String storeAddress;
    @SerializedName("category")
    private String storeCategory;

    public Store(String name, Float lat, Float lon, int id, String address, String category) {
        this.storeName = name;
        this.storeLat = lat;
        this.storeLong = lon;
        this.storeId = id;
        this.storeAddress = address;
        this.storeCategory = category;
    }

    public String getStoreName() { return storeName; }

    public Float getLatitude() { return storeLat; }

    public Float getLongitude() { return storeLong; }

    public int getStoreId() { return storeId; }

    public String getStoreAddress() { return storeAddress; }

    public String getStoreCategory() { return storeCategory; }

}
