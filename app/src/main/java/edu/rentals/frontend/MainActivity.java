package edu.rentals.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    static final String TAG = MainActivity.class.getSimpleName();
    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;
    static final String googleAPIKey = "AIzaSyCtg1qC5bno0m6lqB4AIWIMvHUgGr7b4ns";
    LatLng latLngFromGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting up Google Places Client --> this is more for current location
        // currently don't need this service but can be a potential for future development
//        Places.initialize(getApplicationContext(), googleAPIKey);
//        PlacesClient placesClient = Places.createClient(this);

        // Button & EditText
        Button srcButton = findViewById(R.id.srcButton);
        EditText inputField = findViewById(R.id.inputAddress);

        // after button click
        // -> get user address
        // -> send to google API
        // -> get data from google API
        // -> modify data received from google API
        // -> send request to server
        // -> get response from server
        // -> load data into recycler view

        // Search Button onClick
        srcButton.setOnClickListener(view -> {

            String usrAddress = inputField.getText().toString();
            System.out.println("User Address : " + usrAddress);

            // but need to remember that it is asynchronous
            // need to do a check to make sure result is received
            connectToGoogleAPI(usrAddress);

            // don't start this until hearing back from google api
            // Intent intent = new Intent(getApplicationContext(), SearchShopActivity.class);
            // startActivity(intent);

        });
    }

    // Send Places request to Google Places API to get lat & long
    private void connectToGoogleAPI(String address) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIza...")
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
                // Once successfully get the response, need to use recyclerView and
                // pass the data into SearchStoreActivity view
            }

            @Override
            public void onFailure(Call<Store> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }
}