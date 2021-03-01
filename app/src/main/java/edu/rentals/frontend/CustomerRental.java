package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class CustomerRental {
    @SerializedName("invoice")
    private JSONObject customerRental;

    public CustomerRental(JSONObject customerRental) {
        this.customerRental = customerRental;
    }

    public JSONObject getCustomerRental() {
        return customerRental;
    }
}
