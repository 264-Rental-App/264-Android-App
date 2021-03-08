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
import com.google.firebase.auth.UserProfileChangeRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerSignUpActivity extends AppCompatActivity {

    static Retrofit retrofit;
    static final String BASE_URL = "http://35.222.193.76/";
    static final String TAG = CustomerSignUpActivity.class.getSimpleName();

    EditText c_email;
    EditText c_password;
    EditText c_firstname;
    EditText c_phonenumber;
    static final String ACCOUNT_TYPE = "customer";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutomer_sign_up);

        c_email = findViewById(R.id.s_store_name);
        c_password = findViewById(R.id.s_signup_password);
        c_firstname = findViewById(R.id.s_signup_firstname);
        c_phonenumber = findViewById(R.id.s_store_contact);

        Button backToMain = findViewById(R.id.backToMainFromCSignUp);
        backToMain.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);

        });

        mAuth = FirebaseAuth.getInstance();

        // just take email & password & first name & phone number?
        Button submitCInfo = findViewById(R.id.createStoreInfoBtn);
        submitCInfo.setOnClickListener(v -> createNewUser());
        submitCInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.CustomerSignUpActivity.this, CustomerHomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createNewUser() {
        System.out.println("Here?");

        String email = c_email.getText().toString();
        String password = c_password.getText().toString();
        String firstName = c_firstname.getText().toString();
        String phoneNumber = c_phonenumber.getText().toString();

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
                                Toast.makeText(CustomerSignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(firstName).build();

                                user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");

                                            String uid = user.getUid();

                                            // these information will be stored in the database
                                            User newUser = new User(uid, firstName, email, phoneNumber, ACCOUNT_TYPE);
                                            createNewClientInDatabase(newUser);

                                            startActivity(new Intent(CustomerSignUpActivity.this, CustomerHomeActivity.class));

                                            finish();
                                        }
                                    });

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(CustomerSignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
            else {
                c_email.setError("Please enter an email");
            }
        }
        else if(email.isEmpty()) {
            c_email.setError("Please enter an email to proceed");
        }
        else if(password.isEmpty()) {
            c_password.setError("Please enter a password");
        }
        else if(firstName.isEmpty()) {
            c_firstname.setError("Please give us your name? :)");
        }
    }


    private void createNewClientInDatabase(User user) {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        UserAccountAPIServices userAccountService = retrofit.create(UserAccountAPIServices.class);

        Call<NewUserResponse> call = userAccountService.createClient(user);
        call.enqueue(new Callback<NewUserResponse>() {
            @Override
            public void onResponse(Call<NewUserResponse> call, Response<NewUserResponse> response) {
                // TODO: idk, start customer page activity?

            }

            @Override
            public void onFailure(Call<NewUserResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());

                // TODO: idk, maybe not this page
                Intent intent = new Intent(getApplicationContext(), SearchFailPage.class);
                startActivity(intent);
            }
        });
    }

}
