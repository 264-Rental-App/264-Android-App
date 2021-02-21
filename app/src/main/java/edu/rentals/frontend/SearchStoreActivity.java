package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchStoreActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private StoreList storeList;

    static final String TAG = MainActivity.class.getSimpleName();
    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;
    static final String googleAPIKey = "AIz...";
    LatLng latLngFromGoogle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_store);

        linearLayoutManager = new LinearLayoutManager(this);

        Intent intent = getIntent();
        String userAddress = intent.getStringExtra("userAddress");

        TextView usrAddressView = findViewById(R.id.userAddressView);
        usrAddressView.setText(userAddress);


        // but need to remember that it is asynchronous
        // need to do a check to make sure result is received
        connectToGoogleAPI(userAddress);


        Button backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(view -> {
           Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
           startActivity(mainIntent);
        });


        // start of shopping flow
        final Button btnEquipmentList = findViewById(R.id.btnEquipmentList);
        btnEquipmentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EquipmentListActivity.class);
                startActivity(intent);
            }
        });
    }


    // Send Places request to Google Places API to get lat & long
    private void connectToGoogleAPI(String address) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIza..")
                .build();

        GeocodingApiRequest req = GeocodingApi.newRequest(context).address(address);
        System.out.println("Request url: " + req);
        // Async
        req.setCallback(new PendingResult.Callback<GeocodingResult[]>() {
            @Override
            public void onResult(GeocodingResult[] result) {
                // Handle successful request.

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                System.out.println(gson.toJson(result[0].geometry.location));

                latLngFromGoogle = result[0].geometry.location;

                // Once receive response from google, pass the latitude & longitude to server
                connectToGetShopsFromServer(latLngFromGoogle.lat, latLngFromGoogle.lng);
            }

            @Override
            public void onFailure(Throwable e) {
                // Handle error
                Log.e(TAG, e.toString());

                // Need to think of how to handle on failure
                // Blank page and tell user nothing is found? Try search again?

            }
        });

    }

    // Send request to localhost server to fetch shop list
    private void connectToGetShopsFromServer(double latitude, double longitude) {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        StoreListAPIService shopAPIService = retrofit.create(StoreListAPIService.class);
        Call<Store> call = shopAPIService.getShopList(latitude, longitude);
        call.enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {

                recyclerView = findViewById(R.id.rvStoreList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(new StoreListAdapter(storeList));
            }

            @Override
            public void onFailure(Call<Store> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());

                // Need to think of how to handle on failure
                // Blank page and tell user nothing is found? Try search again?

            }
        });
    }
}
