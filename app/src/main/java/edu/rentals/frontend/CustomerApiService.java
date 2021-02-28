package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CustomerApiService {

    //http://localhost:8080/invoices
    @GET("invoices")
    Call<InvoiceList> getInvoiceList();

    // http://localhost:8080/rental/{invoiceId}
    @GET("rental/{invoiceId}")
    Call<CustomerRental> getRentalInfo(@Path("invoiceId") String invoiceId);

    // http://localhost:8080/users/{userId}
    @GET("users/{userId}")
    Call<Customer> getUserInfo(@Path("userId") String userId);

}
