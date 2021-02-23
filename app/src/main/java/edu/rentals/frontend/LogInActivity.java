package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {

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

    }

}
