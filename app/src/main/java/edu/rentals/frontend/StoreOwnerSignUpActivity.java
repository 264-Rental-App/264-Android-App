package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.UserProfileChangeRequest;

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

        s_email = findViewById(R.id.s_store_name);
        s_password = findViewById(R.id.s_signup_password);
        s_firstname = findViewById(R.id.s_signup_firstname);
        s_phonenumber = findViewById(R.id.s_store_contact);

        Button test = findViewById(R.id.createStoreInfoBtn_test);
        test.setOnClickListener(v -> {
           Intent intent = new Intent(getApplicationContext(), StoreOwnerCreatesStoreActivity.class);
           intent.putExtra("ownerId", "aabbccdd");
           startActivity(intent);
        });


        Button backToHome = findViewById(R.id.backToMainFromSSignUp);
        backToHome.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();

        // just take email & password & first name & phone number?
        Button submitSInfo = findViewById(R.id.createStoreInfoBtn);
        submitSInfo.setOnClickListener(v -> createNewUser());

    }

    private void createNewUser() {

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

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(firstName).build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");

                                                    String uid = user.getUid();

                                                    // these information will be stored in the database
                                                    // TODO: create a head in the createNewOwnerInDatabaserequest?
                                                    User newOwner = new User(uid, firstName, email, phoneNumber);
                                                    createNewOwnerInDatabase(newOwner);
//
                                                    // TODO: The redirected page should be store owner's personal page not MainActivity, need to change
                                                    startActivity(new Intent(StoreOwnerSignUpActivity.this, MainActivity.class));
                                                    finish();
                                                }
                                            });

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
    private void createNewOwnerInDatabase(User user) {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        UserAccountAPIServices userAccountService = retrofit.create(UserAccountAPIServices.class);
        Call<NewUserResponse> call = userAccountService.createOwner(user);
        call.enqueue(new Callback<NewUserResponse>() {
            @Override
            public void onResponse(Call<NewUserResponse> call, Response<NewUserResponse> response) {

                if(response.body() != null) {
                    goCreateNewStore(response.body().getUserId());
                }
                else {
                    // TODO: There should be a better way to handle this
                    Toast.makeText(StoreOwnerSignUpActivity.this, "Could not create account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewUserResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());

                Intent intent = new Intent(getApplicationContext(), SearchFailPage.class);
                startActivity(intent);
            }
        });
    }

    private void goCreateNewStore(String ownerId) {
        Intent intent = new Intent(getApplicationContext(), StoreOwnerCreatesStoreActivity.class);
        intent.putExtra("ownerId", ownerId);
        startActivity(intent);
    }

}
