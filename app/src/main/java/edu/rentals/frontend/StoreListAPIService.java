package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface StoreListAPIService {

    // https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
//    @GET("geocode/json?")
//    Call


    // http://localhost:8080/search/stores?lat=x&long=y
    @GET("search/stores")
    Call<Store> getShopList(@Query("lat=") double lat, @Query ("&long=") double lng);

}
