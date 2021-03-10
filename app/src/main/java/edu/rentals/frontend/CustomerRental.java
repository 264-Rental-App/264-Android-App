package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

public class CustomerRental {
    @SerializedName("id")
    private long invoiceId;

    @SerializedName("totalCost")
    private Double totalCost;

    @SerializedName("transactionDate")
    private String transactionDate;

    @SerializedName("storeId")
    private long storeId;

    @SerializedName("storeName")
    private String storeName;

    @SerializedName("rentalStartDate")
    private String rentalStartDate;

    @SerializedName("dueDate")
    private String dueDate;

    @SerializedName("equipment")
    private List<JSONObject> equipmentList;

    public CustomerRental(long invoiceId, Double totalCost, String transactionDate, long storeId, String storeName, String rentalStartDate, String dueDate, List<JSONObject> equipmentList) {
        this.invoiceId = invoiceId;
        this.totalCost = totalCost;
        this.transactionDate = transactionDate;
        this.storeId = storeId;
        this.storeName = storeName;
        this.rentalStartDate = rentalStartDate;
        this.dueDate = dueDate;
        this.equipmentList = equipmentList;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public long getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getRentalStartDate() {
        return rentalStartDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public List<JSONObject> getEquipmentList() {
        return equipmentList;
    }
}
