package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface shopListAPIService {

    // http://localhost:8080/search/stores?lat=x&long=y
    @GET("search/stores")
    Call<Shop> getShopList(@Query("lat=") int lat, @Query ("&long=") int lon);

}
