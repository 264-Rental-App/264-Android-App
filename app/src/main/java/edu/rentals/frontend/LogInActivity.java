package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.maps.errors.ApiException;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usrNameBox = findViewById(R.id.userName);
        EditText pwBox = findViewById(R.id.userPassword);

        String usrName = usrNameBox.getText().toString();
        String pw = pwBox.getText().toString();


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


        // TODO: Set up authentication

        // TODO: Google sign in option
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.google_sign_in_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.google_sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
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

//            updateUI(account);
        } catch (Exception e) {
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCodeString());
            Log.w(TAG, "signInResult:failed:: " + e.getLocalizedMessage());
//            updateUI(null);
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // TODO: Need to define this .updateUI()
        // updateUI(account);

    }

}
