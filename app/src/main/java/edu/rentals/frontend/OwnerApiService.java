package edu.rentals.frontend;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OwnerApiService {

    // http://localhost:8080/users/{userId}
    @GET("users/{userId}")
    Call<Customer> getUserInfo(@Path("userId") String userId);

    // http://localhost:8080/invoices/{storeId}
    @GET("invoices/{storeId}")
    Call<InvoiceList> getInvoiceList(@Path("storeId") String storeId);

    // http://localhost:8080/rental/{invoiceId}
    @GET("rental/{invoiceId}")
    Call<CustomerRental> getRentalInfo(@Path("invoiceId") int invoiceId);

    // http://localhost:8080/stores/{storeId}
    @GET("stores/{storeId}")
    Call<StoreInfo> getStoreInfo(@Path("storeId") long storeId);

    // http://localhost:8080/equipment/{equipmentId}
    @GET("equipment/{equipmentId}")
    Call<EquipmentInfo> getEquipmentInfo(@Path("equipmentId") int equipmentId);

    // http://localhost:8080/equipment/{storeId}
    @GET("equipment/{storeId}")
    Call<StoreEquipmentList> getEquipmentList(@Path("storeId") long storeId);

    // http://localhost:8080/equipment/{equipmentId}
    @DELETE("equipment/{equipmentId}")
    Call<Void> deleteEquipment(@Path("equipmentId") int equipmentId);

    // http://localhost:8080/equipment
    @POST("equipment")
    Call<OwnerPublishEquipment> publishEquipment(@Body OwnerPublishEquipment equipment);



}
