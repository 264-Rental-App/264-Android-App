package edu.rentals.frontend;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button srcButton;
    EditText inputField;
    Button loginButton;
    BroadcastReceiver broadcastReceiver;
    BroadcastReceiver broadcastReceiver_t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Timer boardcast reciver
        broadcastReceiver_t = new RentalTimerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(broadcastReceiver_t, intentFilter);


        mAuth = FirebaseAuth.getInstance();

        // setting up Google Places Client --> this is more for current location
        // currently don't need this service but can be a potential for future development
//        Places.initialize(getApplicationContext(), googleAPIKey);
//        PlacesClient placesClient = Places.createClient(this);

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

        // TODO: This is temporary, should delete after everything is set up and Google map service
        //  is reactivated
        Button failPageButton = findViewById(R.id.temp_fail);

        failPageButton.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), SearchFailPage.class);
            startActivity(intent);

        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //network connection status boardcast reciver
        broadcastReceiver = new InternetstateReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            loginButton.setText("HOME");
            loginButton.setOnClickListener(v -> goHome());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try{
            unregisterReceiver(broadcastReceiver_t);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void goHome() {
        startActivity(new Intent(this, CustomerHomeActivity.class));
        finish();
    }

}