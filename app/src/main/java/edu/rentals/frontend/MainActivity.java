package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // setting up Google Places Client --> this is more for current location
        // currently don't need this service but can be a potential for future development
//        Places.initialize(getApplicationContext(), googleAPIKey);
//        PlacesClient placesClient = Places.createClient(this);

        // Button & EditText
        Button srcButton = findViewById(R.id.srcButton);
        EditText inputField = findViewById(R.id.inputAddress);


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


        Button loginButton = findViewById(R.id.logInBtn_login_page);

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

}