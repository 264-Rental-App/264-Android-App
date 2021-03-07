package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class EquipmentInfo {
    @SerializedName("equipment")
    private JSONObject equipmentInfo;

    public EquipmentInfo(JSONObject equipmentInfo) {
        this.equipmentInfo = equipmentInfo;
    }

    public JSONObject getStoreInfo() {
        return equipmentInfo;
    }
}
