package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;
    Button back;
    private long storeId;
    TextView tvStoreName, tvAddress, tvPhone;
    private String storeName, address, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_store_edit);

        // get storeId
        storeId = 1;

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
        tvStoreName.setText("Adam's Bike Shop");
        tvAddress.setText("1234 College Av., Irvine, CA 91919");
        tvPhone.setText("8888888888");

        // get customer info
        connect();
    }

    private void connect() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        OwnerApiService ownerApiService = retrofit.create(OwnerApiService.class);

        // api call get customer info
        Call<StoreInfo> storeInfoCall = ownerApiService.getStoreInfo(storeId);
        storeInfoCall.enqueue(new Callback<StoreInfo>() {

            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                // get customer info
                JSONObject customerInfo = response.body().getStoreInfo();


                // set customer info
                try {
                    storeName = customerInfo.get("name").toString();
                    address = customerInfo.get("commonAddress").toString();
                    phoneNumber = customerInfo.get("phoneNumber").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // set text
                tvStoreName.setText(storeName);
                tvAddress.setText(address);
                tvPhone.setText(phoneNumber);

            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
                Log.e(TAG, t.toString());
            }

        });

        // Todo: update info patch

    }
}
