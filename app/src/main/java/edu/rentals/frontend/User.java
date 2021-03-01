package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    private String username;

    // TODO: figure out what to do with password

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("address")
    private String address;

    @SerializedName("email")
    private String email;

    @SerializedName("phone_number")
    private int phone_number;

    public User(String userName, String firstName, String lastName, String address, String email, int phoneNumber) {
        this.username = userName;

        // TODO: Figure out what to do with pw

        this.first_name = firstName;
        this.last_name = lastName;
        this.address = address;
        this.email = email;
        this.phone_number = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public int getPhoneNumber() {
        return phone_number;
    }

}
