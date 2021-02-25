package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerSignUpActivity extends AppCompatActivity {

    static Retrofit retrofit;
    static final String BASE_URL = "http://localhost:8080/";
    static final String TAG = CustomerSignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutomer_sign_up);

        Button backToMain = findViewById(R.id.backToMainFromCSignUp);
        backToMain.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);

        });

        Button submitCInfo = findViewById(R.id.submitCInfo);
        submitCInfo.setOnClickListener(view -> {

            // TODO-1: Gather all user information and build HTTP request
            // TODO-2: Need to verify each EditText box

            // TODO: Need to verify for unique username
            EditText c_username = findViewById(R.id.c_signup_username);

            // TODO: hash or send to oAuth?
            EditText c_password = findViewById(R.id.c_signup_password);

            EditText c_firstname = findViewById(R.id.c_signup_firstname);
            EditText c_lastname = findViewById(R.id.c_signup_lastname);
            EditText c_address = findViewById(R.id.c_signup_address);
            EditText c_email = findViewById(R.id.c_signup_email);
            EditText c_phonenumber = findViewById(R.id.c_signup_phonenumber);

            
            // TODO: Can't directly call the createNewUser, need to go through oAuth first

//            createNewUser(c_username.getText().toString(),
//                                        c_firstname.getText().toString(),
//                                        c_lastname.getText().toString(),
//                                        c_address.getText().toString(),
//                                        c_email.getText().toString(),
//                                        c_phonenumber.getText().toString());


        });

    }

    private void createNewUser(String username, String firstname, String lastname,
                                             String address, String email, String phonenumber) {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        UserAccountAPIServices userAccountService = retrofit.create(UserAccountAPIServices.class);
        Call<User> call = userAccountService.createUser(username, firstname, lastname, address,
                                                        email, phonenumber);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // TODO: idk, start customer page activity?

            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());

                // TODO: idk, maybe not this page
                Intent intent = new Intent(getApplicationContext(), SearchFailPage.class);
                startActivity(intent);
            }
        });
    }

}
