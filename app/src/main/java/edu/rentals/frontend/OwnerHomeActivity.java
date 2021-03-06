package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OwnerHomeActivity extends AppCompatActivity {
    Button search, manageStore, editInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);

        // search button
        search = findViewById(R.id.searchPage);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerHomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // store button
        manageStore = findViewById(R.id.manageStore);
        manageStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerHomeActivity.this, OwnerStoreActivity.class);
                startActivity(intent);
            }
        });


        // edit info button
        editInfo = findViewById(R.id.editOwner);
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerHomeActivity.this, OwnerEditActivity.class);
                startActivity(intent);
            }
        });
    }




}
