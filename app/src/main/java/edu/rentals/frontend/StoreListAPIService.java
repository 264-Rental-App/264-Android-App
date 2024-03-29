package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface StoreListAPIService {

    // http://localhost:8080/search/stores?lat=x&long=y
    @GET("search/stores")
    Call<StoreList> getShopList(@Query("lat") float lat, @Query("long") float lng);

}
