package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OwnerStoreActivity extends AppCompatActivity implements OwnerStoreAdapter.EquipmentListClickListener {
    Button back, editStore, add;
    private long storeId;
    private static List<Equipment> ownerEquipmentList;
    static final String TAG = OwnerStoreActivity.class.getSimpleName();
    static final String BASE_URL = "http://35.222.193.76/";
    static Retrofit retrofit = null;
    private RecyclerView recyclerView;
    private OwnerStoreAdapter eAdapter;
    FirebaseUser mUser;

    private String idToken;

    private static int positionChosen;

    public static Long getEquipmentId() {
        return ownerEquipmentList.get(positionChosen).getEquipmentId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_store);

        // get storeId
//        storeId = 1;


        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerStoreActivity.this, OwnerHomeActivity.class);
                startActivity(intent);
            }
        });

        // edit store info
        editStore = findViewById(R.id.editStore);
        editStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerStoreActivity.this, OwnerStoreEditActivity.class);
                startActivity(intent);
            }
        });

        // add new item
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerStoreActivity.this, OwnerStoreAddActivity.class);
                startActivity(intent);
            }
        });

        // mock invoice list
        ownerEquipmentList = new ArrayList<>();
//        ownerEquipmentList.add(new Equipment(1, "Bike", 500, 1, 10));
//        ownerEquipmentList.add(new Equipment(3, "Snowboard", 300, 1, 20));
//        ownerEquipmentList.add(new Equipment(5, "Ski", 300, 1, 30));
//        ownerEquipmentList.add(new Equipment(7, "Scooter", 500, 1, 40));
//        ownerEquipmentList.add(new Equipment(7, "Helmet", 50, 1, 40));

        // recycleView
        recyclerView = findViewById(R.id.equipmentListRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));


//        // connect api call for store equipment list
//        connect();

        // adapter
        eAdapter = new OwnerStoreAdapter(ownerEquipmentList, this);
        recyclerView.setAdapter(eAdapter);

    }

    private void getStoreIdCall(String idToken) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        OwnerApiService ownerApiService = retrofit.create(OwnerApiService.class);

        // api call for storeId
        Call<Store> storeIdCall = ownerApiService.getStoreId(idToken);
        storeIdCall.enqueue(new Callback<Store>() {

            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {

                storeId = response.body().getStoreId();
                System.out.println("storeId in getStoreId: " + storeId);
                connect(idToken);
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void connect(String idToken) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        OwnerApiService ownerApiService = retrofit.create(OwnerApiService.class);

        // api call for equipment list
        System.out.println("storeId for equipment list" + storeId);
        Call<StoreEquipmentList> invoiceListCall = ownerApiService.getEquipmentList(idToken, storeId);
        invoiceListCall.enqueue(new Callback<StoreEquipmentList>() {

            @Override
            public void onResponse(Call<StoreEquipmentList> call, Response<StoreEquipmentList> response) {
                // get equipment list
                List<LinkedTreeMap> equipmentList = response.body().getStoreEquipmentList();
//                System.out.println("reponse body: " + );

                // set invoice list
                for (int i = 0; i < equipmentList.size(); i++) {
                    System.out.println("print out" + equipmentList.get(i));
                    Long equipmentId = (Long) Math.round((Double) equipmentList.get(i).get("id"));
                    String equipmentName = equipmentList.get(i).get("name").toString();
                    int stock = (int) Math.round((Double) equipmentList.get(i).get("stock"));
                    String desc = equipmentList.get(i).get("description").toString();
//                    int price = (double) equipmentList.get(i).get("price");
                    int price = (int) Math.round((Double) equipmentList.get(i).get("price"));

                    ownerEquipmentList.add(new Equipment(equipmentId, equipmentName, price, 1, stock));
                }
                eAdapter = new OwnerStoreAdapter(ownerEquipmentList, OwnerStoreActivity.this::onInvoiceClick);
                recyclerView.setAdapter(eAdapter);

            }

            @Override
            public void onFailure(Call<StoreEquipmentList> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onInvoiceClick(int position) {
        positionChosen = position;
        Intent intent = new Intent(edu.rentals.frontend.OwnerStoreActivity.this, OwnerStoreUpdateActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO: Get current user's idToken
        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        String name = mUser.getDisplayName();
//        TextView tvFirstName = findViewById(R.id.userFirstName);
//        tvFirstName.setText(name);
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            getStoreIdCall(idToken);

                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

}
