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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerEditActivity extends AppCompatActivity {
    static final String TAG = EquipmentListActivity.class.getSimpleName();
    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;

    Button back;
    private int userId;
    private String userName, firstName, lastName, email, phoneNumber;

    TextView tvUserName, tvFirstName, tvLastName, tvEmail, tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit);

        // user id
        userId = 1;

        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.CustomerEditActivity.this, CustomerHomeActivity.class);
                startActivity(intent);
            }
        });

        //text view customer info
        tvUserName = findViewById(R.id.userNameCurr);
        tvFirstName = findViewById(R.id.firstNameCurr);
        tvLastName = findViewById(R.id.lastNameCurr);
        tvEmail = findViewById(R.id.userEmailCurr);
        tvPhone = findViewById(R.id.phoneNumberCurr);

        // mock set
        tvUserName.setText("adamHa");
        tvFirstName.setText("Adam");
        tvLastName.setText("Ha");
        tvEmail.setText("a@gmail.com");
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
        Call<Customer> customerInfoCall = customerApiService.getUserInfo(String.valueOf(userId));
        customerInfoCall.enqueue(new Callback<Customer>() {

            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                // get customer info
                JSONObject customerInfo = response.body().getCustomerInfo();


                // set customer info
                try {
                    userName = customerInfo.get("userName").toString();
                    firstName = customerInfo.get("firstName").toString();
                    lastName = customerInfo.get("lastName").toString();
                    email = customerInfo.get("email").toString();
                    phoneNumber = customerInfo.get("phoneNumber").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // set text
                tvUserName.setText(userName);
                tvFirstName.setText(firstName);
                tvLastName.setText(lastName);
                tvEmail.setText(email);
                tvPhone.setText(phoneNumber);

            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e(TAG, t.toString());
            }

        });
    }
}
