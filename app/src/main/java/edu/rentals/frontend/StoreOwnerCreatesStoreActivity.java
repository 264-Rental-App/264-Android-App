package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
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

public class StoreOwnerCreatesStoreActivity extends AppCompatActivity {

    static final String TAG = StoreOwnerCreatesStoreActivity.class.getSimpleName();
    static final String BASE_URL = "http://35.222.193.76/";
    static Retrofit retrofit = null;
    static final String googleAPIKey = "AIzaSyDawlfjDVj5dqOjeiOqkHg6D2WR2OkaQaI";
    LatLng latLngFromGoogle;

    EditText storeName;
    EditText storeStreet;
    EditText storeCity;
    EditText storeState;
    EditText storeZipCode;
    EditText storeContact;
    Spinner storeCategory;

    String storeNameStr;
    String street;
    String city;
    String state;
    String zipCode;
    String ownerId;
    String storeContactNum;
    String category;
    String address;

    Button createStoreBtn;

    private String idToken;
    FirebaseUser mUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);

        // get store owner's id
        Intent intent = getIntent();
        ownerId = intent.getStringExtra("ownerId");

        storeName = findViewById(R.id.s_store_name);
        storeStreet = findViewById(R.id.s_street_address);
        storeCity = findViewById(R.id.s_city);
        storeState = findViewById(R.id.s_state);
        storeZipCode = findViewById(R.id.s_zip_code);
        storeContact = findViewById(R.id.s_store_contact);
        storeCategory = findViewById(R.id.s_category_spinner);

        createStoreBtn = findViewById(R.id.createStoreInfoBtn);
        // TODO: call google to get lat & long, then relay to sending request to server
        createStoreBtn.setOnClickListener(v -> getLatLong());

        Button test = findViewById(R.id.createStoreInfoBtn_test2);
        test.setOnClickListener(v -> {

            System.out.println("Store name: " + storeNameStr);
            System.out.println("Street address: " + street);
            System.out.println("city: " + city);
            System.out.println("State: " + state);
            System.out.println("zip code: " + zipCode);
            System.out.println("contact number: " + storeContactNum);
            System.out.println("category: " + category);

            startActivity(new Intent(this, OwnerHomeActivity.class));
        });
        
    }

    private void getLatLong() {

        storeNameStr = storeName.getText().toString();
        street = storeStreet.getText().toString();
        city = storeCity.getText().toString();
        state = storeState.getText().toString();
        zipCode = storeZipCode.getText().toString();
        storeContactNum = storeContact.getText().toString();
        category = storeCategory.getSelectedItem().toString();

        address = street + ", " + city + ", " + state + " " + zipCode;

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(googleAPIKey)
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

                // Once receive response from google, create a Store object, pass to server
                PostNewStore store = new PostNewStore(storeNameStr, latLngFromGoogle.lat, latLngFromGoogle.lng,
                                        ownerId, address, category, storeContactNum);

                mUser = FirebaseAuth.getInstance().getCurrentUser();
                mUser.getIdToken(true)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    idToken = task.getResult().getToken();
                                    createStore(idToken, store);
                                    // Send token to your backend via HTTPS
                                    // ...
                                } else {
                                    // Handle error -> task.getException();
                                    task.getException().printStackTrace();
                                }
                            }
                        });

            }

            @Override
            public void onFailure(Throwable e) {
                // Handle error
                Log.e(TAG, e.toString());

                // Need to think of how to handle on failure
                // Blank page and tell user nothing is found? Try search again?
                Intent intent = new Intent(getApplicationContext(), SearchFailPage.class);
                startActivity(intent);

            }
        });
    }

    private void createStore(String idToken, PostNewStore store) {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        StoreAPIService storeService = retrofit.create(StoreAPIService.class);
        Call<Store> call = storeService.createStore(idToken, store);
        call.enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {

                // Redirecting to Owner's home page
                Toast.makeText(StoreOwnerCreatesStoreActivity.this, "Successfully Created Store!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StoreOwnerCreatesStoreActivity.this, OwnerHomeActivity.class));

            }

            @Override
            public void onFailure(Call<Store> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());

                  Intent intent = new Intent(getApplicationContext(), SearchFailPage.class);
                  startActivity(intent);
            }
        });
    }




}
