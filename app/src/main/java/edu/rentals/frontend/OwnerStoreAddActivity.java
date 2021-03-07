package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OwnerStoreAddActivity extends AppCompatActivity {
    static final String TAG = OwnerStoreAddActivity.class.getSimpleName();
    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;
    Button back, update;
    private long storeId;

    private String idToken;

    EditText etName, etCost, etImgLoc, etQuantity, etDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_store_add);

        // get storeId
        storeId = 1;

        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerStoreAddActivity.this, OwnerStoreActivity.class);
                startActivity(intent);
            }
        });

        // link edit text
        etName = findViewById(R.id.equipmentNameAdd);
        etCost = findViewById(R.id.priceAdd);
        etImgLoc = findViewById(R.id.imgLocAdd);
        etQuantity = findViewById(R.id.quantityAdd);
        etDesc = findViewById(R.id.descriptionAdd);

        // add new item
        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNewEquipment();
                    Intent intent = new Intent(edu.rentals.frontend.OwnerStoreAddActivity.this, OwnerStoreActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(v.getContext(),
                            "input error",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });
    }

    private void addNewEquipment() {

        // get text from edit text
        String name = etName.getText().toString();
        float cost = Float.parseFloat(etCost.getText().toString());
        String imgLoc = etImgLoc.getText().toString();
        int quantity = Integer.parseInt(etQuantity.getText().toString());
        String description = etDesc.getText().toString();

        //
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        OwnerApiService ownerApiService = retrofit.create(OwnerApiService.class);
        OwnerPublishEquipment equipment = new OwnerPublishEquipment(storeId, name, cost, imgLoc, quantity, description);
        Call<OwnerPublishEquipment> call = ownerApiService.publishEquipment(idToken, equipment);
        call.enqueue(new Callback<OwnerPublishEquipment>() {
            @Override
            public void onResponse(Call<OwnerPublishEquipment> call, Response<OwnerPublishEquipment> response) {

            }

            @Override
            public void onFailure(Call<OwnerPublishEquipment> call, Throwable t) {
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
