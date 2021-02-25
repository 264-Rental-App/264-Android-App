package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAccountAPIServices {

    // TODO: Need to figure out what to do with password
    // http://localhost:8080/users
    @POST("users")
    Call<User> createUser(@Query("username=") String username, @Query("&firstname=") String firstName,
                           @Query("&lastname=") String lastName, @Query("&address=") String address,
                           @Query("&email=") String email, @Query("&phonenumber=") String phoneNumber);

}
