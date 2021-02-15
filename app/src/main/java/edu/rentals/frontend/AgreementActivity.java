package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AgreementActivity extends AppCompatActivity {
    Button Agree, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.AgreementActivity.this, EquipmentListActivity.class);
                startActivity(intent);

            }
        });

        // checkout
        Agree = findViewById(R.id.agree);
        Agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.AgreementActivity.this, SummaryActivity.class);
                startActivity(intent);
            }
        });
    }
}
