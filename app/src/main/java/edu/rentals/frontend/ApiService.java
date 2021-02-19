package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    // http://localhost:8080/equipment/{storeId}
    @GET("equipment/{storeId}")
    Call<EquipmentList> getEquipmentList(@Path("storeId") String storeId);
}
