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

import java.util.ArrayList;
import java.util.List;

public class EquipmentListActivity extends AppCompatActivity {
    private static List<Equipment> equipmentList;
    private RecyclerView recyclerView;
    private EquipmentListAdapter eAdapter;
    Button checkOut;
    Button back;
    TextView totalSum;
    private int storeId; // get from StoreList.java
    private String storeName; // get using stroreId
    private static int total = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_list);

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
        equipmentList = new ArrayList<>();
        equipmentList.add(new Equipment("Bike", 50, R.drawable.bike, 0));
        equipmentList.add(new Equipment("Ski", 100, R.drawable.ski, 0));
        equipmentList.add(new Equipment("Snowboard", 150, R.drawable.snowboard, 0));
//        equipmentList.add(new Equipment("Helmet", 10, R.drawable.snowboard));

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
