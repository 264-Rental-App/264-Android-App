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
import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OwnerStoreEditActivity extends AppCompatActivity {
    static final String TAG = OwnerStoreEditActivity.class.getSimpleName();
    static final String BASE_URL = "http://35.222.193.76/";
    static Retrofit retrofit = null;
    Button back;
    private long storeId;
    TextView tvStoreName, tvAddress, tvPhone;
    private String storeName, address, phoneNumber;
    private String idToken;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_store_edit);

        // get storeId
//        storeId = 1;
//        storeId = getStoreIdCall();

        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerStoreEditActivity.this, OwnerStoreActivity.class);
                startActivity(intent);
            }
        });

        //text view owner info
        tvStoreName = findViewById(R.id.storeNameCurr);
        tvAddress = findViewById(R.id.addressCurr);
        tvPhone = findViewById(R.id.phoneNumberCurr);

        // mock set
//        tvStoreName.setText("Adam's Bike Shop");
//        tvAddress.setText("1234 College Av., Irvine, CA 91919");
//        tvPhone.setText("8888888888");

        // get customer info
//        connect();
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
                connect(idToken, storeId);
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }

    private void connect(String idToken, long storeId) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        OwnerApiService ownerApiService = retrofit.create(OwnerApiService.class);

        // api call get customer info
        Call<Store> storeInfoCall = ownerApiService.getStoreInfo(idToken, storeId);
        storeInfoCall.enqueue(new Callback<Store>() {

            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                // get customer info
                storeName = response.body().getStoreName();
                address = response.body().getStoreAddress();
                phoneNumber = response.body().getPhoneNumber();

                System.out.println("storeName ");


                // set text
                tvStoreName.setText(storeName);
                tvAddress.setText(address);
                tvPhone.setText(phoneNumber);

            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                Log.e(TAG, t.toString());
            }

        });

        // Todo: update info patch

    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO: Get current user's idToken
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            getStoreIdCall(idToken);

                        } else {
                            // Handle error -> task.getException();
                            task.getException().printStackTrace();
                        }
                    }
                });
        System.out.println("idToken: " + idToken);
    }

}
