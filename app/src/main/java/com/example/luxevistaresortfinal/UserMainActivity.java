package com.example.luxevistaresortfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserMainActivity extends AppCompatActivity {

    Button AddRoomBTN, bookRoomBTN, reserveServiceButton, addServiceButton, viewProfileButton, viewAttractionsButton;
    String email;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        AddRoomBTN = findViewById(R.id.btnAddRoom);
        bookRoomBTN = findViewById(R.id.btnBookRoom);
        reserveServiceButton = findViewById(R.id.btnReserveServices);
        addServiceButton = findViewById(R.id.btnAddServices);
        viewProfileButton = findViewById(R.id.btnProfile);
        viewAttractionsButton = findViewById(R.id.btnAttractions);

        email = getIntent().getStringExtra("user_email");

        AddRoomBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainActivity.this, AddRoomActivity.class);
                startActivity(intent);
            }
        });

        bookRoomBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainActivity.this, ViewRoomsActivity.class);
                intent.putExtra("user_email", email);
                startActivity(intent);
            }
        });

        reserveServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainActivity.this, ViewServicesActivity.class);
                intent.putExtra("user_email", email);
                startActivity(intent);
            }
        });

        addServiceButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserMainActivity.this, AddServiceActivity.class);
            startActivity(intent);
        });

        viewProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserMainActivity.this, UserProfileActivity.class);
            intent.putExtra("user_email", email);
            startActivity(intent);
        });

        viewAttractionsButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserMainActivity.this, HotelInfoActivity.class);
            startActivity(intent);
        });
    }
}