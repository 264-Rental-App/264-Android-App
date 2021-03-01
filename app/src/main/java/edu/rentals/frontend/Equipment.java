package edu.rentals.frontend;

public class Equipment {
    int equipmentId;
    String eName;
    int ePrice;
    int ePhoto;
    int quantity;

    public Equipment(int equipmentId, String eName, int ePrice, int ePhoto, int quantity) {
        this.equipmentId = equipmentId;
        this.eName = eName;
        this.ePrice = ePrice;
        this.ePhoto = ePhoto;
        this.quantity = quantity;
    }

    public int getEquipmentId() {
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
