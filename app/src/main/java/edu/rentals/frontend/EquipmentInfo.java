package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class EquipmentInfo {
//    @SerializedName("equipment")
//    private JSONObject equipmentInfo;

    @SerializedName("id")
    private Long id;

    @SerializedName("storeId")
    private Long storeId;

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private String category;

    @SerializedName("price")
    private Float price;

    @SerializedName("stock")
    private Integer stock;

    @SerializedName("description")
    private String description;

    public EquipmentInfo(Long id, Long storeId, String name, String category, Float price, Integer stock, String description) {
        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }
}
