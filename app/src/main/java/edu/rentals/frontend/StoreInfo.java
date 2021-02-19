package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class StoreInfo {
    @SerializedName("store")
    private static JSONObject storeInfo;

    @SerializedName("title")
    private String title;

    public StoreInfo(String title) {
        this.title = title;
    }
//    public StoreInfo(JSONObject storeInfo) {
//        this.storeInfo = storeInfo;
//    }

    public static JSONObject getStoreInfo() {
        String string = "{ \"store\": {\"name\": \"Big Store\",\"lat\": 130,\"long\": 122,\"id\": 1,\"commonAddress\": \"CA 91711\",\"phoneNumber\": 9999999999 }";
        try {
            storeInfo = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storeInfo;
    }

    public String getTitle() {
        return title;
    }
}
