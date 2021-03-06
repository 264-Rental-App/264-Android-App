package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OwnerStoreActivity extends AppCompatActivity {
    Button back, editStore, add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_store);

        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerStoreActivity.this, OwnerHomeActivity.class);
                startActivity(intent);
            }
        });

        // edit store info
        editStore = findViewById(R.id.editStore);
        editStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerStoreActivity.this, OwnerStoreEditActivity.class);
                startActivity(intent);
            }
        });

        // add new item
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerStoreActivity.this, OwnerStoreAddActivity.class);
                startActivity(intent);
            }
        });

    }
}
