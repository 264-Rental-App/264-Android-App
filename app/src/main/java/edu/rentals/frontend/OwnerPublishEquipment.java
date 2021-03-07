package edu.rentals.frontend;

public class OwnerPublishEquipment {
    private long storeId;
    private String name;
    private float cost;
    private String imgLoc;
    private int quantity;
    private String description;

    public OwnerPublishEquipment(long storeId, String name, float cost, String imgLoc, int quantity, String description) {
        this.storeId = storeId;
        this.name = name;
        this.cost = cost;
        this.imgLoc = imgLoc;
        this.quantity = quantity;
        this.description = description;
    }
}
