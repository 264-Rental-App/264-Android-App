package edu.rentals.frontend;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OwnerApiService {

    // http://localhost:8080/users/{userId}
    @GET("users/{userId}")
    Call<User> getUserInfo(@Header("Authorization") String idToken, @Path("userId") String userId);

    // http://localhost:8080/invoices/{storeId}
    @GET("invoices/{storeId}")
    Call<InvoiceList> getInvoiceList(@Header("Authorization") String idToken, @Path("storeId") String storeId);

    // http://localhost:8080/rental/{invoiceId}
    @GET("rental/{invoiceId}")
    Call<CustomerRental> getRentalInfo(@Header("Authorization") String idToken, @Path("invoiceId") int invoiceId);

    // http://localhost:8080/stores
    @GET("stores")
    Call<Store> getStoreId(@Header("Authorization") String idToken);

    // http://localhost:8080/stores/{storeId}
    @GET("stores/{storeId}")
    Call<Store> getStoreInfo(@Header("Authorization") String idToken, @Path("storeId") long storeId);

    // http://localhost:8080/equipment/{equipmentId}
    @GET("equipment/{equipmentId}")
    Call<EquipmentInfo> getEquipmentInfo(@Header("Authorization") String idToken, @Path("equipmentId") Long equipmentId);

    // http://localhost:8080/equipment/store/{storeId}
    @GET("equipment/store/{storeId}")
    Call<StoreEquipmentList> getEquipmentList(@Header("Authorization") String idToken, @Path("storeId") long storeId);

    // http://localhost:8080/equipment/{equipmentId}
    @DELETE("equipment/{equipmentId}")
    Call<Void> deleteEquipment(@Header("Authorization") String idToken, @Path("equipmentId") Long equipmentId);

    // http://localhost:8080/equipment
    @POST("equipment")
    Call<OwnerPublishEquipment> publishEquipment(@Header("Authorization") String idToken, @Body OwnerPublishEquipment equipment);



}
