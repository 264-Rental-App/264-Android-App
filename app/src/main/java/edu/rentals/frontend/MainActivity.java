package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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


public class MainActivity extends AppCompatActivity {

    static final String TAG = MainActivity.class.getSimpleName();
    static final String BASE_URL = "http://35.222.193.76:80/";
    static Retrofit retrofit = null;

    FirebaseAuth mAuth;
    Button srcButton;
    EditText inputField;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Button & EditText
        srcButton = findViewById(R.id.srcButton);
        inputField = findViewById(R.id.inputAddress);
        loginButton = findViewById(R.id.logInBtn_login_page);


        // Search Button onClick
        srcButton.setOnClickListener(view -> {

            String usrAddress = inputField.getText().toString();
            System.out.println("User Address : " + usrAddress);
            // validating input
            if(usrAddress == null || usrAddress.equals("") || usrAddress.equals(" ") || !usrAddress.matches("[a-zA-Z0-9\\s,.-]*")) {
                System.out.println("Invalid address input");
                inputField.setText("");
                inputField.setError("Please enter valid address.");
                return;
            }

            Intent intent = new Intent(getApplicationContext(), SearchStoreActivity.class);
            intent.putExtra("userAddress", usrAddress);
            startActivity(intent);

        });

        loginButton.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);

        });
    }


    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            String uid = currentUser.getUid();

            loginButton.setText("HOME");
            loginButton.setOnClickListener(v -> {
                currentUser.getIdToken(true)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                verifyAccountType(idToken, uid);

                            } else {
                                // Handle error -> task.getException();
                                task.getException().printStackTrace();
                            }
                        });
            });
        }

    }


    private void verifyAccountType(String idToken, String uid) {

        System.out.println("GOT HEREEREERREREE");

        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        UserAccountAPIServices userAccountApiServices = retrofit.create(UserAccountAPIServices.class);

        System.out.println("idToken is: " + idToken);
        System.out.println("uid is: " + uid);

        Call<GetUserById> call = userAccountApiServices.getUserById(idToken, uid);
        call.enqueue(new Callback<GetUserById>() {
            @Override
            public void onResponse(Call<GetUserById> call, Response<GetUserById> response) {
                assert response.body() != null : "response.body() is null!";

                if(response.body().getAccountType().equals("customer")) {
                    startActivity(new Intent(MainActivity.this, CustomerHomeActivity.class));
                }
                else if(response.body().getAccountType().equals("owner")) {
                    startActivity(new Intent(MainActivity.this, OwnerHomeActivity.class));
                }
            }

            @Override
            public void onFailure(Call<GetUserById> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());

                startActivity(new Intent(MainActivity.this, SearchFailPage.class));
            }
        });

    }

}