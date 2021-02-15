package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreList {

    @SerializedName("stores")
    public List<Store> storeList;

    public StoreList(List<Store> shops) {
        this.storeList = shops;
    }
}
