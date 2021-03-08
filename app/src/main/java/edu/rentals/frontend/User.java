package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("userId")
    private String userId;

    @SerializedName("userFirstName")
    private String userFirstName;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("userPhoneNumber")
    private String userPhoneNumber;

    private String accountType;

    public User(String userId, String firstName, String email, String phoneNumber, String accountType) {
        this.userId = userId;
        this.userFirstName = firstName;
        this.userEmail = email;
        this.userPhoneNumber = phoneNumber;
        this.accountType = accountType;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirst_name() {
        return userFirstName;
    }

    public String getEmail() {
        return userEmail;
    }

    public String getPhoneNumber() {
        return userPhoneNumber;
    }

    public String getAccountType() {
        return accountType;
    }
}
