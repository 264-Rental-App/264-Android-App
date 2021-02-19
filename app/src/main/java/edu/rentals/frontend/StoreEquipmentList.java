package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreEquipmentList {
    @SerializedName("equipment")
    private List equipmentList;

    public StoreEquipmentList(List equipmentList) {
        this.equipmentList = equipmentList;
    }

    public List getStoreEquipmentList() {
        return equipmentList;
    }
}


