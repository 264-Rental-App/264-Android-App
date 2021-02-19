package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class StoreInfo {
    @SerializedName("store")
    private JSONObject storeInfo;

    public StoreInfo(JSONObject storeInfo) {
        this.storeInfo = storeInfo;
    }

    public JSONObject getStoreInfo() {
        return storeInfo;
    }
}
