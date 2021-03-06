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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerSignUpActivity extends AppCompatActivity {

    static Retrofit retrofit;
    static final String BASE_URL = "http://localhost:8080/";
    static final String TAG = CustomerSignUpActivity.class.getSimpleName();

    EditText c_email;
    EditText c_password;
    EditText c_firstname;
    EditText c_phonenumber;

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
    }

    private void createNewUser() {
        System.out.println("Here?");

        // TODO-1: Gather all user information and build HTTP request
        // TODO-2: Need to verify each EditText box

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

                                String uid = user.getUid();

                                // these information will be stored in the database
                                User newUser = new User(uid, firstName, email, phoneNumber);
                                createNewClientInDatabase(newUser);

                                // TODO: start Store Owner Home page, but probably from createNewClientDatabase
                                startActivity(new Intent(CustomerSignUpActivity.this, MainActivity.class));

                                // TODO: send token to the back --> or just add a header section in the createNewClientInDatabase Request?

                                finish();
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

        Call<User> call = userAccountService.createClient(user);
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
