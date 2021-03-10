package edu.rentals.frontend;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.List;

public class ShoppingCheckoutRental {
    private long storeId;
    private String userId;
    private List<Equipment> equipment;
    private String rentalTime, returnTime;

    public ShoppingCheckoutRental(long storeId, String userId, String rentalTime, String returnTime, List<Equipment> equipment) {
        this.storeId = storeId;
        this.userId = userId;
        this.equipment = equipment;
        this.rentalTime = rentalTime;
        this.returnTime = returnTime;
    }
}
