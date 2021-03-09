package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAccountAPIServices {

    // /users/{userId}
    @GET("users/{userId}")
    Call<GetUserById> getUserById(@Header("Authorization") String idToken, @Path("userId") String uid);

    // http://localhost:8080/users
    @POST("users")
    Call<NewUserResponse> createClient(@Body User user);

    // http://localhost:8080/users
    @POST("users")
    Call<NewUserResponse> createOwner(@Body User user);

}
