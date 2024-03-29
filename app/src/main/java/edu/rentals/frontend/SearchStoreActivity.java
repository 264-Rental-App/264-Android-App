package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchStoreActivity extends AppCompatActivity implements OnStoreListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private StoreList storeList;
    private String userAddress;

    static final String TAG = MainActivity.class.getSimpleName();
    static final String BASE_URL = "http://35.222.193.76/";
    static Retrofit retrofit = null;
    static final String googleAPIKey = "AIzaSyDawlfjDVj5dqOjeiOqkHg6D2WR2OkaQaI";
    LatLng latLngFromGoogle;

//    private OnStoreListener mOnStoreListener;

    FirebaseAuth mAuth;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_store);

        // TODO: NEED TO VERIFY IF IT WORKS
//        mOnStoreListener = new SearchStoreActivity();
        linearLayoutManager = new LinearLayoutManager(this);
        TextView usrAddressView = findViewById(R.id.userAddressView);
        usrAddressView.setText(userAddress);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        userAddress = intent.getStringExtra("userAddress");

        login = findViewById(R.id.loginBtn_search_store);
        login.setOnClickListener(view -> {

            Intent loginIntent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(loginIntent);

        });

        connectToGoogleAPI(userAddress);
//        fakeGoogleAPICall((userAddress));

        Button backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(view -> {
           Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
           startActivity(mainIntent);
        });

    }


    // Send Places request to Google Places API to get lat & long
    private void connectToGoogleAPI(String address) {
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

                // Once receive response from google, pass the latitude & longitude to server
                System.out.println("Latitude is : " + latLngFromGoogle.lat);
                System.out.println("Longitude is : " + latLngFromGoogle.lng);

                connectToGetShopsFromServer((float)latLngFromGoogle.lat, (float)latLngFromGoogle.lng);
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

    // Send request to localhost server to fetch shop list
    private void connectToGetShopsFromServer(float latitude, float longitude) {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        StoreListAPIService shopAPIService = retrofit.create(StoreListAPIService.class);
        Call<StoreList> call = shopAPIService.getShopList(latitude, longitude);
        call.enqueue(new Callback<StoreList>() {
            @Override
            public void onResponse(Call<StoreList> call, Response<StoreList> response) {

                storeList = response.body();

                recyclerView = findViewById(R.id.rvStoreList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(new StoreListAdapter(storeList, SearchStoreActivity.this));
            }

            @Override
            public void onFailure(Call<StoreList> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());

                  Intent intent = new Intent(getApplicationContext(), SearchFailPage.class);
                  startActivity(intent);
            }
        });
    }

    private void fakeGoogleAPICall(String address) {
        returningFakeInformationToUser(30.0, 40.0);
    }

    private void returningFakeInformationToUser(double latitude, double longitude) {

//        Store store1 = new Store("Bike Shop", 33.3, 50.5, (long)0, "SHDFKJH35Sd", "1223 Blah Street, Some City, CA 93333", "Bike", "1112223333");
//        Store store2 = new Store("Random Shop", 33.3, 50.5, (long)1, "SHDFKJH35Sd", "1400 Random Street, Some City, CA 93333", "Bike", "1112223333");
//        Store store3 = new Store("Surrey Shop", 33.3, 50.5, (long)2, "SHDFKJH35Sd", "1500 Whatever Street, Some City, CA 93333", "Bike", "1112223333");
//        Store store4 = new Store("Surrey Shop", 33.3, 50.5, (long)3, "SHDFKJH35Sd", "1223 Blah Street, Some City, CA 93333", "Bike", "1112223333");
//        Store store5 = new Store("Surfboard Shop", 33.3, 50.5, (long)4, "SHDFKJH35Sd", "1223 Blah Street, Some City, CA 93333", "Bike", "1112223333");
//        Store store6 = new Store("Bike Shop", 33.3, 50.5, (long)5, "SHDFKJH35Sd", "1500 Whatever Street, Some City, CA 93333", "Bike", "1112223333");
//        Store store7 = new Store("Bike Shop", 33.3, 50.5, (long)6, "SHDFKJH35Sd", "1223 Blah Street, Some City, CA 93333", "Bike", "1112223333");
//        List<Store> list = new ArrayList<>(Arrays.asList(store1, store2, store3, store4, store5, store6, store7));
//
//        storeList = new StoreList(list);
//
//        recyclerView = findViewById(R.id.rvStoreList);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(new StoreListAdapter(storeList, this));

    }


    @Override
    public void onStoreClicked(long storeId) {


//            // this gives a reference to the store that gets selected
//            System.out.println("Inside the onStoreClicked :" + storeList.storeList.size());
//            Store selectedStore = storeList.storeList.get(position);
//            System.out.println("Selected: ");
//            System.out.println(selectedStore.getStoreName());
//            System.out.println("Store ID is : " + selectedStore.getStoreId());

        // start shopping flow
        Intent intent = new Intent(this, EquipmentListActivity.class);
        System.out.println("storeId in search" + storeId);
        intent.putExtra("storeID", storeId);
        intent.putExtra("userAddress", userAddress);
        startActivity(intent);

    }


    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            login.setText("HOME");
            login.setOnClickListener(v -> {
                String uid = currentUser.getUid();

                currentUser.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            verifyAccountType(idToken, uid);

                        } else {
                            // Handle error -> task.getException();
                            task.getException().printStackTrace();
                        }
                    });

            });
        }
    }

    private void verifyAccountType(String idToken, String uid) {

        System.out.println("GOT HEREEREERREREE");

        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        UserAccountAPIServices userAccountApiServices = retrofit.create(UserAccountAPIServices.class);

        System.out.println("idToken is: " + idToken);
        System.out.println("uid is: " + uid);

        Call<GetUserById> call = userAccountApiServices.getUserById(idToken, uid);
        call.enqueue(new Callback<GetUserById>() {
            @Override
            public void onResponse(Call<GetUserById> call, Response<GetUserById> response) {
                assert response.body() != null : "response.body() is null!";

                if(response.body().getAccountType().equals("customer")) {
                    startActivity(new Intent(SearchStoreActivity.this, CustomerHomeActivity.class));
                }
                else if(response.body().getAccountType().equals("owner")) {
                    startActivity(new Intent(SearchStoreActivity.this, OwnerHomeActivity.class));
                }
            }

            @Override
            public void onFailure(Call<GetUserById> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());

                startActivity(new Intent(SearchStoreActivity.this, SearchFailPage.class));
            }
        });

    }

}
