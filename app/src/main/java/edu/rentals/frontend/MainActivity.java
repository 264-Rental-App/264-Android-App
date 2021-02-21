package edu.rentals.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
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
            // validating input
            if(usrAddress == null || usrAddress.equals("") || usrAddress.equals(" ") || !usrAddress.matches("[a-zA-Z0-9\\s,.-]*")) {
                System.out.println("Invalid address input");
                inputField.setText("");
                inputField.setError("Please enter valid address.");
                return;
            }


            // don't start this until hearing back from google api
            // need to put extra for user's original address as well
             Intent intent = new Intent(getApplicationContext(), SearchStoreActivity.class);
             intent.putExtra("userAddress", usrAddress);
             startActivity(intent);

        });
    }

}