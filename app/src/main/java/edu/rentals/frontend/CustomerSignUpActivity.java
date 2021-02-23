package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutomer_sign_up);

        Button backToMain = findViewById(R.id.backToMainFromCSignUp);
        backToMain.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);

        });

        Button submitCInfo = findViewById(R.id.submitCInfo);
        submitCInfo.setOnClickListener(view -> {

            // TODO: Gather all user information and build HTTP request

        });

    }
}
