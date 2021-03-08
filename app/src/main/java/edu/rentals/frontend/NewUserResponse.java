package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

public class NewUserResponse {
    @SerializedName("userId")
    private String userId;

    public NewUserResponse(String userId) {
        this.userId = userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
