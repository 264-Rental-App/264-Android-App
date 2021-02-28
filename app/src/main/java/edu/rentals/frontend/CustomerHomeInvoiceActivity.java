package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerHomeInvoiceActivity extends AppCompatActivity {
    private int invoiceId;
    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_invoice);

        // get invoiceId
        invoiceId = CustomerHomeActivity.getInvoiceId();
        Log.d("position", String.valueOf(invoiceId));

        // search
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.CustomerHomeInvoiceActivity.this, CustomerHomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
