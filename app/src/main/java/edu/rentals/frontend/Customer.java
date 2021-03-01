package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Customer {
    @SerializedName("user")
    private JSONObject customerInfo;

    public Customer(JSONObject customerInfo) {
        this.customerInfo = customerInfo;
    }

    public JSONObject getCustomerInfo() {
        return customerInfo;
    }
}
