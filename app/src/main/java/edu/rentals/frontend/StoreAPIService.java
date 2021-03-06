package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface StoreAPIService {

    @POST("stores")
    Call<Store> createStore(@Body PostNewStore newStore);

}
