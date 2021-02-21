package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ShoppingApiService {

    //http://localhost:8080/stores/{storeId}
//    @GET("stores/{storeId}")
//    Call<StoreInfo> getStoreInfo(@Path("storeId") String storeId);

    // http://localhost:8080/equipment/{storeId}
    @GET("equipment/{storeId}")
    Call<StoreEquipmentList> getEquipmentList(@Path("storeId") String storeId);

    // http://localhost:8080/rental/forms/{storeId}
//    @GET("rental/forms/{storeId}")
//    Call<AgreementForm> getAgreementForm(@Path("storeId") String storeId);

}
