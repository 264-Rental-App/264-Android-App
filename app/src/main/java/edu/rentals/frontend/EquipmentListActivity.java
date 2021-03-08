package edu.rentals.frontend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EquipmentListActivity extends AppCompatActivity {
    static final String TAG = EquipmentListActivity.class.getSimpleName();
    private static List<Equipment> equipmentList;
    private RecyclerView recyclerView;
    private EquipmentListAdapter eAdapter;
    Button checkOut;
    Button back;
    Button login;
    TextView totalSum;
    TextView tvStoreName, tvStoreAddress, tvStoreNumber;
    private long storeId = 0;
    private String storeName, storeAddress, storeNumber;
    private static int total = 0;

    static final String BASE_URL = "http://35.222.193.76/";
    static Retrofit retrofit = null;
    FirebaseAuth mAuth;

    // info that this view should hold onto
    private String usrAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_list);

        // get storeId from SearchStoreActivity
        Intent intent = getIntent();
        storeId = (long) intent.getIntExtra("storeID", 0);


        mAuth = FirebaseAuth.getInstance();



        connect();

        usrAddress = intent.getStringExtra("userAddress");

        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SearchStoreActivity.class);
                intent.putExtra("userAddress", usrAddress);

                startActivity(intent);

            }
        });

        login = findViewById(R.id.equip_login);
        login.setOnClickListener(v -> startActivity(new Intent(EquipmentListActivity.this, LogInActivity.class)));


        // equipment list
        equipmentList = new ArrayList<>();
//        equipmentList.add(new Equipment(1, "Bike", 50, R.drawable.bike, 0));
//        equipmentList.add(new Equipment(2, "Ski", 100, R.drawable.ski, 0));
//        equipmentList.add(new Equipment(3, "Snowboard", 150, R.drawable.snowboard, 0));
//        equipmentList.add(new Equipment(4, "Helmet", 10, 0, 0));
//        equipmentList.add(new Equipment(5, "Snow Pants", 30, 0, 0));

        // recycleView
        recyclerView = findViewById(R.id.equipmentListRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));

        // adapter
        eAdapter = new EquipmentListAdapter(equipmentList);
        recyclerView.setAdapter(eAdapter);


        // total sum
        totalSum = findViewById(R.id.priceSum);
        eAdapter.setOnTotalPriceChangeListener(new EquipmentListAdapter.OnTotalPriceChangeListener() {
            @Override
            public void onTotalPriceChanged(int totalPrice) {
                Log.d("total : ", String.valueOf(totalPrice));
                total = totalPrice;
                totalSum.setText(" $" + String.valueOf(totalPrice));
            }
        });

        // checkout
        checkOut = findViewById(R.id.checkOut);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.EquipmentListActivity.this, AgreementActivity.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });

    }


    private void connect() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        ShoppingApiService shoppingApiService = retrofit.create(ShoppingApiService.class);


        // api call store info
        Call<StoreInfo> storeInfoCall = shoppingApiService.getStoreInfo(String.valueOf(storeId));
        storeInfoCall.enqueue(new Callback<StoreInfo>() {

            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                // Set store info
                JSONObject storeInfo = response.body().getStoreInfo();

                //store info
                tvStoreName = findViewById(R.id.shopName);
                tvStoreAddress = findViewById(R.id.shopAddress);
                tvStoreNumber = findViewById(R.id.shopPhone);

                // set store info
                try {
                    storeName = storeInfo.get("name").toString();
                    storeAddress = storeInfo.get("commonAddress").toString();
                    storeNumber = storeInfo.get("phoneNumber").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // set text
                tvStoreName.setText(storeName);
                tvStoreAddress.setText(storeAddress);
                tvStoreNumber.setText(storeNumber);
            }

            @Override
            public void onFailure(Call< StoreInfo > call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        // api call store equipment list
        Call<StoreEquipmentList> call = shoppingApiService.getEquipmentList(String.valueOf(storeId));
        call.enqueue(new Callback<StoreEquipmentList>() {

            @Override
            public void onResponse(Call<StoreEquipmentList> call, Response<StoreEquipmentList> response) {
                // Set equipment list name and price
                List<LinkedTreeMap> storeEquipmentList = response.body().getStoreEquipmentList();
                for (int i = 0; i < storeEquipmentList.size(); i++) {
                    int id = (int) storeEquipmentList.get(i).get("id");
                    String equipmentName = storeEquipmentList.get(i).get("name").toString();
                    int equipmentPrice = (int) storeEquipmentList.get(i).get("price");
                    equipmentList.add(new Equipment(id, equipmentName, equipmentPrice, 0, 0));
                }
                recyclerView.setAdapter(new EquipmentListAdapter(equipmentList));
            }

            @Override
            public void onFailure(Call< StoreEquipmentList > call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    public static List<Equipment> getEquipmentList() {
        return equipmentList;
    }



    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            checkOut.setEnabled(false);
            checkOut.setBackgroundColor(Color.LTGRAY);
        }
        else {
            login.setText("HOME");
            login.setOnClickListener(v -> startActivity(new Intent(EquipmentListActivity.this, CustomerHomeActivity.class)));
        }
    }



}
