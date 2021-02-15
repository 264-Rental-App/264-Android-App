package edu.rentals.frontend;

public class Equipment {
    String eName;
    int ePrice;
    int ePhoto;
    int quantity;
    int subTotal;

    public Equipment(String eName, int ePrice, int ePhoto, int quantity) {
        this.eName = eName;
        this.ePrice = ePrice;
        this.ePhoto = ePhoto;
        this.quantity = quantity;
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

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }


}
