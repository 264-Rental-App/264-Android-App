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

        // after button click
        // -> get user address
        // -> send to google API
        // -> get data from google API
        // -> modify data received from google API
        // -> send request to server
        // -> get response from server
        // -> load data into recycler view

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


            // don't start this until hearing back from google api
            // need to put extra for user's original address as well
             Intent intent = new Intent(getApplicationContext(), SearchStoreActivity.class);
             intent.putExtra("userAddress", usrAddress);
             startActivity(intent);


        });
    }

}