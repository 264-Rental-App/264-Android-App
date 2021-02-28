package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAccountAPIServices {

    // http://localhost:8080/users
    @POST("users")
    Call<User> createClient(@Query("&email=") String email, @Query("&firstname=") String firstName,
                          @Query("&phonenumber=") String phoneNumber);

    // http://localhost:8080/users
    @POST("owners")
    Call<User> createOwner(@Query("&email=") String email, @Query("&firstname=") String firstName,
                          @Query("&phonenumber=") String phoneNumber);

}
