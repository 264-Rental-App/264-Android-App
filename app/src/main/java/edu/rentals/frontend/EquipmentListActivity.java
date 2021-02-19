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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private String storeName; // get using stroreId
    private static int total = 0;

    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_list);
//        connect();

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
        storeName = "Shop Name";


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
                Log.d("total : ",String.valueOf(totalPrice));
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

                SaveCart();
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
        Call<StoreInfo> call = shoppingApiService.getStoreInfo(String.valueOf(storeId));
        call.enqueue(new Callback<StoreInfo>() {

            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                // Set store name and info
                TextView tvShopName = findViewById(R.id.shopName);
                TextView tvShopAddress = findViewById(R.id.shopAddress);
                TextView tvShopNumber = findViewById(R.id.shopPhone);
                JSONObject shopInfo = response.body().getStoreInfo();
                try {
                    String shopName = shopInfo.get("name").toString();
                    String shopAddress = shopInfo.get("commonAddress").toString();
                    String shopNumber = shopInfo.get("phoneNumber").toString();
                    tvShopName.setText(shopName);
                    tvShopAddress.setText(shopAddress);
                    tvShopNumber.setText(shopNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }


    private boolean SaveCart() {

        return true;
    }


//    @Override
//    public void onUserInteraction() {
//        Log.d("total",String.valueOf(eAdapter.getTotalPrice()));
//        totalSum.setText(String.valueOf(eAdapter.getTotalPrice()));
//    }


    public static List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public static int getTotal() {
        return total;
    }
}
