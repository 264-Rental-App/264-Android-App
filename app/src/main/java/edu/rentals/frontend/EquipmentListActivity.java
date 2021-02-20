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

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EquipmentListActivity extends AppCompatActivity {
    static final String TAG = EquipmentListActivity.class.getSimpleName();
    private static List<Equipment> equipmentList = new ArrayList<>();;
    private RecyclerView recyclerView;
    private EquipmentListAdapter eAdapter;
    Button checkOut;
    Button back;
    TextView totalSum;
    private int storeId = 0; // get from StoreList.java
    private String storeName, storeAddress, storeNumber; // get from previous activity
    private static int total = 0;

    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_list);
        connect();

        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.EquipmentListActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        // store name and info
        // use get methods from searching flow
        storeName = "Shop Name";
        storeAddress = "";
        storeNumber = "";
        TextView tvStoreName = findViewById(R.id.shopName);
        TextView tvStoreAddress = findViewById(R.id.shopAddress);
        TextView tvStoreNumber = findViewById(R.id.shopPhone);
//        tvStoreName.setText("");
//        tvStoreAddress.setText("");
//        tvStoreNumber.setText("");

        // equipment list
        equipmentList.add(new Equipment("Bike", 50, R.drawable.bike, 0));
        equipmentList.add(new Equipment("Ski", 100, R.drawable.ski, 0));
        equipmentList.add(new Equipment("Snowboard", 150, R.drawable.snowboard, 0));
        equipmentList.add(new Equipment("Helmet", 10, 0, 0));
        equipmentList.add(new Equipment("Snow Pants", 30, 0, 0));

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
                startActivity(intent);

//                SaveCart();
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
        Call<StoreEquipmentList> call = shoppingApiService.getEquipmentList(String.valueOf(storeId));
        call.enqueue(new Callback<StoreEquipmentList>() {

            @Override
            public void onResponse(Call<StoreEquipmentList> call, Response<StoreEquipmentList> response) {
                // Set equipment list name and price
                List<LinkedTreeMap> storeEquipmentList = response.body().getStoreEquipmentList();
                for (int i = 0; i < storeEquipmentList.size(); i++) {
                    String equipmentName = storeEquipmentList.get(i).get("name").toString();
                    int equipmentPrice = (int) storeEquipmentList.get(i).get("price");
                    equipmentList.add(new Equipment(equipmentName, equipmentPrice, 0, 0));
                }
                recyclerView.setAdapter(new EquipmentListAdapter(equipmentList));
            }

            @Override
            public void onFailure(Call< StoreEquipmentList > call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }


//    private boolean SaveCart() {
//
//        return true;
//    }


//    @Override
//    public void onUserInteraction() {
//        Log.d("total",String.valueOf(eAdapter.getTotalPrice()));
//        totalSum.setText(String.valueOf(eAdapter.getTotalPrice()));
//    }


    public static List<Equipment> getEquipmentList() {
        return equipmentList;
    }

}
