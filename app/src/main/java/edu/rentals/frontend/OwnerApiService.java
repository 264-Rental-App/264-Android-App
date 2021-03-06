package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OwnerApiService {
    // http://localhost:8080/invoices/{storeId}
    @GET("invoices/{storeId}")
    Call<InvoiceList> getInvoiceList(@Path("storeId") String storeId);

    // http://localhost:8080/stores/{storeId}
    @GET("stores/{storeId}")
    Call<InvoiceList> getStoreInfo(@Path("storeId") int storeId);



}
