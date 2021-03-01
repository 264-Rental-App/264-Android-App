package edu.rentals.frontend;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

public class Rental {
    private int storeId, userId;
    private List<Equipment> equipment;

    public Rental(int storeId, int userId, List<Equipment> equipment) {
        this.storeId = storeId;
        this.userId = userId;
        this.equipment = equipment;
    }
}
