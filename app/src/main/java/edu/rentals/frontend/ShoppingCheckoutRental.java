package edu.rentals.frontend;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.List;

public class ShoppingCheckoutRental {
    private int storeId;
    private String userId;
    private List<Equipment> equipment;
    private Timestamp startTimestamp, endTimestamp;

    public ShoppingCheckoutRental(int storeId, String userId, Timestamp startTimestamp, Timestamp endTimestamp, List<Equipment> equipment) {
        this.storeId = storeId;
        this.userId = userId;
        this.equipment = equipment;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }
}
