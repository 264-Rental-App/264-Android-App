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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.maps.errors.ApiException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";

    static final String classTAG = MainActivity.class.getSimpleName();
    static final String BASE_URL = "http://35.222.193.76:80/";
    static Retrofit retrofit = null;


    private FirebaseAuth mAuth;
    EditText mEmail;
    EditText mPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.userName);
        mPass = findViewById(R.id.userPassword);

        Button customerSignUpBtn = findViewById(R.id.customerSignUp);
        customerSignUpBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CustomerSignUpActivity.class);
            startActivity(intent);
        });

        Button storeManSignUpBtn = findViewById(R.id.storeManSignUp);
        storeManSignUpBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), StoreOwnerSignUpActivity.class);
            startActivity(intent);
        });

        Button goBackHome = findViewById(R.id.homeFromLogin);
        goBackHome.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        });

        Button signIn = findViewById(R.id.logInBtn_login_page);
        // TODO: Set up authentication
        signIn.setOnClickListener(v -> loginUser());

        // TODO: Google sign in option
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.google_sign_in_button).setOnClickListener(this);

    }

    private void loginUser() {
        String email = mEmail.getText().toString();
        String pw = mPass.getText().toString();

        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if(!email.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, pw)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(LogInActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                                    String uid = mAuth.getCurrentUser().getUid();
                                    startActivity(new Intent(LogInActivity.this, CustomerHomeActivity.class));

                                    // TODO: send the user token to the back
                                    verifyAccountType(uid);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LogInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else {
                mEmail.setError("Please enter email");
            }
        }
        else if(pw.isEmpty()) {
            mPass.setError("Please enter password");
        }
    }

    private void verifyAccountType(String uid) {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        UserAccountAPIServices userAccountApiServices = retrofit.create(UserAccountAPIServices.class);
        Call<GetUserById> call = userAccountApiServices.getUserById(uid);
        call.enqueue(new Callback<GetUserById>() {
            @Override
            public void onResponse(Call<GetUserById> call, Response<GetUserById> response) {
                assert response.body() != null : "response.body() is null!";

                if(response.body().getAccountType().equals("customer")) {
                    startActivity(new Intent(LogInActivity.this, CustomerHomeActivity.class));
                }
                else if(response.body().getAccountType().equals("owner")) {
                    startActivity(new Intent(LogInActivity.this, OwnerHomeActivity.class));
                }
            }

            @Override
            public void onFailure(Call<GetUserById> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());

                  startActivity(new Intent(LogInActivity.this, SearchFailPage.class));
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.google_sign_in_button:
                googleSignIn();
                break;
        }
    }


    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            updateUI(account);
        } catch (Exception e) {
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCodeString());
            Log.w(TAG, "signInResult:failed:: " + e.getLocalizedMessage());
            updateUI(null);
        }
    }


    private void updateUI(GoogleSignInAccount account) {
        System.out.println("Google account name: " + account);
        if(account != null) {
            Toast.makeText(this,"U Signed In successfully", Toast.LENGTH_LONG).show();
//            startActivity(new Intent(this, CustomerPage.class));
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            Toast.makeText(this,"Sign In Failed", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // Probably don't need to worry about this, there won't be login option if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null) {
            // TODO: Need to define this .updateUI()
            updateUI(account);
        }
    }


    // TODO: Gotta finish this auto-redirecting
    private void reload() {

        // TODO: Redirect straight into Customer / Store manager's page
        //  if account is already logged in

    }


}
