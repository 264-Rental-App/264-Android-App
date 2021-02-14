package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

public class Store {

    @SerializedName("name")
    private String shopName;
    @SerializedName("lat")
    private Float shopLat;
    @SerializedName("long")
    private Float shopLong;
    @SerializedName("id")
    private int shopId;
    @SerializedName("commonAddress")
    private String shopAddress;

    public Store(String name, Float lat, Float lon, int id, String address) {
        this.shopName = name;
        this.shopLat = lat;
        this.shopLong = lon;
        this.shopId = id;
        this.shopAddress = address;
    }

    public String getShopName() { return shopName; }

    public Float getLatitude() { return shopLat; }

    public Float getLongitude() { return shopLong; }

    public int getShopId() { return shopId; }

    public String getShopAddress() { return shopAddress; }

}
