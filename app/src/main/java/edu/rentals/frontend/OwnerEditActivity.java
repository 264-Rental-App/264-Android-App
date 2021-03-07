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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OwnerEditActivity extends AppCompatActivity {
    static final String TAG = OwnerEditActivity.class.getSimpleName();
    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;
    Button back;
    private String userId;
    private String firstName, email, phoneNumber;
    TextView tvFirstName, tvEmail, tvPhone;
    private String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_edit);

        // get user id
        userId = "1";

        // back button
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerEditActivity.this, OwnerHomeActivity.class);
                startActivity(intent);
            }
        });

        //text view owner info
        tvEmail = findViewById(R.id.emailCurr);
        tvFirstName = findViewById(R.id.firstNameCurr);
        tvPhone = findViewById(R.id.phoneNumberCurr);

        // mock set
        tvEmail.setText("a@gmail.com");
        tvFirstName.setText("Adam");
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
        CustomerApiService customerApiService = retrofit.create(CustomerApiService.class);

        // api call get customer info
        Call<Customer> customerInfoCall = customerApiService.getUserInfo(idToken, userId);
        customerInfoCall.enqueue(new Callback<Customer>() {

            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                // get customer info
                JSONObject customerInfo = response.body().getCustomerInfo();


                // set customer info
                try {
                    firstName = customerInfo.get("firstName").toString();
                    email = customerInfo.get("email").toString();
                    phoneNumber = customerInfo.get("phoneNumber").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // set text
                tvEmail.setText(email);
                tvFirstName.setText(firstName);
                tvPhone.setText(phoneNumber);

            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
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
