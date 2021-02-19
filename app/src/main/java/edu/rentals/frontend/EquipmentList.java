package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EquipmentList {
    @SerializedName("equipment")
    private List equipmentList;

    public EquipmentList(List equipmentList) {
        this.equipmentList = equipmentList;
    }

    public List getEquipmentList() {
        return equipmentList;
    }
}


