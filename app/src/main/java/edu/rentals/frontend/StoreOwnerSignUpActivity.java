package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreOwnerSignUpActivity extends AppCompatActivity {

    static Retrofit retrofit;
    static final String BASE_URL = "http://localhost:8080/";
    static final String TAG = CustomerSignUpActivity.class.getSimpleName();

    EditText s_email;
    EditText s_password;
    EditText s_firstname;
    EditText s_phonenumber;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_reg);

        s_email = findViewById(R.id.s_signup_email);
        s_password = findViewById(R.id.s_signup_password);
        s_firstname = findViewById(R.id.s_signup_firstname);
        s_phonenumber = findViewById(R.id.s_signup_phonenumber);


        Button backToHome = findViewById(R.id.backToMainFromSSignUp);
        backToHome.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();

        // just take email & password & first name & phone number?
        Button submitSInfo = findViewById(R.id.submitSInfo);
        submitSInfo.setOnClickListener(v -> createNewUser());
        submitSInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.StoreOwnerSignUpActivity.this, OwnerHomeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void createNewUser() {
        System.out.println("Here?");

        // TODO-1: Gather all user information and build HTTP request
        // TODO-2: Need to verify each EditText box

        String email = s_email.getText().toString();
        String password = s_password.getText().toString();
        String firstName = s_firstname.getText().toString();
        String phoneNumber = s_phonenumber.getText().toString();

        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if(!email.isEmpty()) {
                // TODO: authenticate
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(StoreOwnerSignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // these information will be stored in the database
                                createNewOwnerInDataBase(email, firstName, phoneNumber);
//
                                    // TODO: The redirected page should be store owner's personal page not MainActivity, need to change
                                    startActivity(new Intent(StoreOwnerSignUpActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(StoreOwnerSignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else {
                s_email.setError("Please enter an email");
            }
        }
        else if(email.isEmpty()) {
            s_email.setError("Please enter an email to proceed");
        }
        else if(password.isEmpty()) {
            s_password.setError("Please enter a password");
        }
        else if(firstName.isEmpty()) {
            s_firstname.setError("Please give us your name? :)");
        }
    }


    // Might not need this
    private void createNewOwnerInDataBase(String email, String firstname, String phonenumber) {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        UserAccountAPIServices userAccountService = retrofit.create(UserAccountAPIServices.class);
        Call<User> call = userAccountService.createOwner(email, firstname, phonenumber);
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
