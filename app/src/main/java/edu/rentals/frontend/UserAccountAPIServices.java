package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAccountAPIServices {

    // http://localhost:8080/users
//    @POST("users")
//    Call<User> createClient(@Query("userId") String uid, @Query("&userFirstName=") String firstName, @Query("&userEmail=") String email,
//                            @Query("&userPhoneNumber=") String phoneNumber);
    @POST("users")
    Call<NewUserResponse> createClient(@Body User user);

    // http://localhost:8080/users
//    @POST("owners")
//    Call<User> createOwner(@Query("userId") String uid, @Query("&userFirstName=") String firstName, @Query("&userEmail=") String email,
//                           @Query("&userPhoneNumber=") String phoneNumber);
    @POST("owners")
    Call<NewUserResponse> createOwner(@Body User user);

}
