package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AgreementActivity extends AppCompatActivity {
    static final String TAG = EquipmentListActivity.class.getSimpleName();
    Button Agree, back;
    TextView tvForm;

    static final String BASE_URL = "http://35.222.193.76/";
    static Retrofit retrofit = null;
    private long storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);


        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.AgreementActivity.this, EquipmentListActivity.class);
                startActivity(intent);

            }
        });

        // get storeId
        Intent intent = getIntent();
        storeId = intent.getLongExtra("storeId", 0);
        System.out.println("storeId in agreement: " + storeId);

        // api call form
        connect();

        // checkout
        Agree = findViewById(R.id.agree);
        Agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.AgreementActivity.this, SummaryActivity.class);
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
        // api call form
        Call<AgreementForm> formCall = shoppingApiService.getAgreementForm(storeId);
        formCall.enqueue(new Callback<AgreementForm>() {

            @Override
            public void onResponse(Call<AgreementForm> call, Response<AgreementForm> response) {
                // Set equipment list name and price
//                List<LinkedTreeMap> formList = response.body().getFormListList();
//                String formString = formList.get(0).get("formBody").toString();
                String formString =  "A rental and lease agreement is a document that outlines the arrangement between an owner of a real estate, known as the “landlord” or “lessor”, and someone else that is willing to pay rent while occupying the property, known as the “tenant” or “lessee”. In layman’s term, it’s a document used for the occupying of space (either commercial or residential) for a period of time in exchange for monthly rent. The terms of the contract are negotiable between the tenant and landlord and once signed, the form is considered legally and mutually binding. ";

                // find view
                tvForm = findViewById(R.id.agreementDescription);

                // set text
                tvForm.setText(formString);
            }

            @Override
            public void onFailure(Call< AgreementForm > call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
