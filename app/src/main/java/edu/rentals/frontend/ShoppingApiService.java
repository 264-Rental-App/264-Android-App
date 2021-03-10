package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ShoppingApiService {

    //http://localhost:8080/stores/{storeId}
    @GET("stores/{storeId}")
    Call<StoreInfo> getStoreInfo(@Path("storeId") String storeId);

    // http://localhost:8080/equipment/{storeId}
    @GET("equipment/store/{storeId}")
    Call<StoreEquipmentList> getEquipmentList(@Header("Authorization") String idToken, @Path("storeId") long storeId);

    // http://localhost:8080/rental/forms/{storeId}
    @GET("rental/forms/{storeId}")
    Call<AgreementForm> getAgreementForm(@Path("storeId") String storeId);

    // http://localhost:8080/rental
    @POST("/rental")
    Call<ShoppingCheckoutRental> createRental(@Header("Authorization") String idToken, @Body ShoppingCheckoutRental rental);

    // http://localhost:8080/invoices



}
