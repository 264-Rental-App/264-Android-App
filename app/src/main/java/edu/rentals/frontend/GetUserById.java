package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

public class GetUserById {

    @SerializedName("userFirstName")
    private String userFirstName;

    @SerializedName("userType")
    private String userType;

    public GetUserById(String userFirstName, String accountType) {
        this.userFirstName = userFirstName;
        this.userType = accountType;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserType(String accountType) {
        this.userType = accountType;
    }

    public String getUserType() {
        return userType;
    }
}
