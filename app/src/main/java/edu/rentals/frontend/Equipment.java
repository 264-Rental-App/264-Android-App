package edu.rentals.frontend;

public class Equipment {
    Long equipmentId;
    String eName;
    int ePrice;
    int ePhoto;
    int quantity;

    public Equipment(Long equipmentId, String eName, int ePrice, int ePhoto, int quantity) {
        this.equipmentId = equipmentId;
        this.eName = eName;
        this.ePrice = ePrice;
        this.ePhoto = ePhoto;
        this.quantity = quantity;
    }

    public Equipment(Long equipmentId, int quantity) {
        this.equipmentId = equipmentId;
        this.quantity = quantity;
    }


    public Long getEquipmentId() {
        return equipmentId;
    }

    public String geteName() {
        return eName;
    }

    public int getePrice() {
        return ePrice;
    }

    public int getePhoto() {
        return ePhoto;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
