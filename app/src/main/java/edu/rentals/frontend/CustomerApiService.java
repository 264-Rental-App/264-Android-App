package edu.rentals.frontend;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface CustomerApiService {

    //http://localhost:8080/invoices
    @GET("invoices")
    Call<InvoiceList> getInvoiceList(@Header("Authorization") String idToken);

    // http://localhost:8080/rental/{invoiceId}
    @GET("rental/{invoiceId}")
    Call<CustomerRental> getRentalInfo(@Header("Authorization") String idToken, @Path("invoiceId") long invoiceId);

    // http://localhost:8080/users/{userId}
    @GET("users/{userId}")
    Call<GetUserById> getUserInfo(@Header("Authorization") String idToken, @Path("userId") String userId);

    // http://localhost:8080/users/{userId}
//    @PATCH("users/{userId}")
//    Call<User> updateUser(@Path("userId") String userId, @Body User user);
}
