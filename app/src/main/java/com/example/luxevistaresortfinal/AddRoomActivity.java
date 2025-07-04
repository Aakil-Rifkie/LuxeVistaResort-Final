package com.example.luxevistaresortfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddRoomActivity extends AppCompatActivity {

    EditText roomNameInput, descriptionInput, priceInput;
    Button submitRoomButton;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        roomNameInput = findViewById(R.id.roomNameInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        priceInput = findViewById(R.id.priceInput);
        submitRoomButton = findViewById(R.id.submitRoomBTN);

        dbHelper = new DatabaseHelper(AddRoomActivity.this);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {

            finish();
        });

        submitRoomButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                String roomName = roomNameInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String price = priceInput.getText().toString();

                if (roomName.isEmpty() || description.isEmpty() || price.isEmpty()) {
                    Toast.makeText(AddRoomActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {

                    double dbprice = Double.parseDouble(price);
                    boolean isInserted = dbHelper.insertRoom(roomName, description, dbprice);

                    if (isInserted){
                        Toast.makeText(AddRoomActivity.this, "Room has been added", Toast.LENGTH_SHORT).show();
                        roomNameInput.setText("");
                        descriptionInput.setText("");
                        priceInput.setText("");

                    } else {
                        Toast.makeText(AddRoomActivity.this, "Error adding room", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}