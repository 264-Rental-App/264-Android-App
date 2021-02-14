package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreList {

    @SerializedName("stores")
    private List<Store> shopList;

    public StoreList(List<Store> shops) {
        this.shopList = shops;
    }
}
