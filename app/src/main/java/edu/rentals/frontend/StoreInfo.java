package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class StoreInfo {
    @SerializedName("store")
    private Store store;

    public StoreInfo(Store store) {
        this.store = store;
    }

    public Store getStoreInfo() {
        return store;
    }

//    @SerializedName("name")
//    private String storeName;
//    @SerializedName("lat")
//    private Double storeLat;
//    @SerializedName("lon")
//    private Double storeLong;
//    @SerializedName("id")
//    private Long id;
//    @SerializedName("commonAddress")
//    private String storeAddress;
//    @SerializedName("category")
//    private String storeCategory;
//    @SerializedName("ownerId")
//    private String ownerId;
//    @SerializedName("phoneNumber")
//    private String phoneNumber;
//
//    public StoreInfo(String storeName, Double storeLat, Double storeLong, Long id, String storeAddress, String storeCategory, String ownerId, String phoneNumber) {
//        this.storeName = storeName;
//        this.storeLat = storeLat;
//        this.storeLong = storeLong;
//        this.id = id;
//        this.storeAddress = storeAddress;
//        this.storeCategory = storeCategory;
//        this.ownerId = ownerId;
//        this.phoneNumber = phoneNumber;
//    }
//
//    public String getStoreName() {
//        return storeName;
//    }
//
//    public Double getStoreLat() {
//        return storeLat;
//    }
//
//    public Double getStoreLong() {
//        return storeLong;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getStoreAddress() {
//        return storeAddress;
//    }
//
//    public String getStoreCategory() {
//        return storeCategory;
//    }
//
//    public String getOwnerId() {
//        return ownerId;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
}
