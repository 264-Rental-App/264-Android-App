package edu.rentals.frontend;

public class NewUserResponse {
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
