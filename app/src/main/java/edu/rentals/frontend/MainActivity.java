package edu.rentals.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    static final String TAG = MainActivity.class.getSimpleName();
    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;
    static final String googleAPIKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting up Google Places Client
        Places.initialize(getApplicationContext(), googleAPIKey);
        PlacesClient placesClient = Places.createClient(this);

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



            // don't start this until hearing back from google api
            // Intent intent = new Intent(getApplicationContext(), SearchShopActivity.class);
            // startActivity(intent);

        });
    }

    // Send Places request to Google Places API to get lat & long
    private void connectToGoogleAPI() {

    }

    // Send request to localhost server to fetch shop list
    private void connectToGetShopsFromServer() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        StoreListAPIService shopAPIService = retrofit.create(StoreListAPIService.class);
        Call<Store> call = shopAPIService.getShopList(100, 200);
    }
}