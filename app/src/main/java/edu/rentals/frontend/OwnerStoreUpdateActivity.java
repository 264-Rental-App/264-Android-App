package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OwnerStoreUpdateActivity extends AppCompatActivity{
    static final String TAG = OwnerStoreUpdateActivity.class.getSimpleName();
    static final String BASE_URL = "http://35.222.193.76/";
    static Retrofit retrofit = null;
    Button back, delete;
    private int equipmentId;
    TextView tvStoreName, tvAddress, tvPhone;
    private String storeName, address, phoneNumber;
    TextView tvName, tvStock, tvPrice, tvDesc;
    private String name, desc;
    private int stock, price;
    private String idToken;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_store_update);

        // get equipmentId
//        equipmentId = 1;
        equipmentId = OwnerStoreActivity.getEquipmentId();
        Log.d("equipmentId", String.valueOf(equipmentId));

        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerStoreUpdateActivity.this, OwnerStoreActivity.class);
                startActivity(intent);
            }
        });


        // api call for equipment info
//        getEquipmentInfo();

        // delete
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.getIdToken(true)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    idToken = task.getResult().getToken();
                                    deleteEquipment(idToken);
                                    Intent intent = new Intent(edu.rentals.frontend.OwnerStoreUpdateActivity.this, OwnerStoreActivity.class);
                                    startActivity(intent);

                                    // Send token to your backend via HTTPS
                                    // ...
                                } else {
                                    // Handle error -> task.getException();
                                    task.getException().printStackTrace();
                                }
                            }
                        });
            }
        });

        // ToDo: path update equipment info
    }

    private void getEquipmentInfo(String idToken) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        OwnerApiService ownerApiService = retrofit.create(OwnerApiService.class);

        // api call to get equipment info
        Call<EquipmentInfo> equipmentInfoCall = ownerApiService.getEquipmentInfo(idToken, equipmentId);
        equipmentInfoCall.enqueue(new Callback<EquipmentInfo>() {

            @Override
            public void onResponse(Call<EquipmentInfo> call, Response<EquipmentInfo> response) {
                // get equipment info
                JSONObject equipmentInfo = response.body().getStoreInfo();

                // set equipment info
                try {
                    name = equipmentInfo.get("name").toString();
                    stock = Integer.parseInt(equipmentInfo.get("stock").toString());
                    desc = equipmentInfo.get("desc").toString();
                    price = Integer.parseInt(equipmentInfo.get("price").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // set text
                tvName.setText(name);
                tvStock.setText(stock);
                tvPrice.setText(desc);
                tvDesc.setText(price);
            }

            @Override
            public void onFailure(Call<EquipmentInfo> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }

    private void deleteEquipment(String idToken) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        OwnerApiService ownerApiService = retrofit.create(OwnerApiService.class);

        // api call to delete equipment
        Call<Void> deleteEquipment = ownerApiService.deleteEquipment(idToken, equipmentId);
        deleteEquipment.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO: Get current user's idToken
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();

                            getEquipmentInfo(idToken);
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                            task.getException().printStackTrace();
                        }
                    }
                });
        System.out.println("idToken: " + idToken);
    }

}
