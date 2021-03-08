package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

public class GetUserById {

    @SerializedName("userFirstName")
    private String userFirstName;

    @SerializedName("accountType")
    private String accountType;

    public GetUserById(String userFirstName, String accountType) {
        this.userFirstName = userFirstName;
        this.accountType = accountType;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }
}
