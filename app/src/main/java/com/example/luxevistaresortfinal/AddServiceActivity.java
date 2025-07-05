package com.example.luxevistaresortfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class AddServiceActivity extends AppCompatActivity {

    EditText serviceNameInput, serviceDescriptionInput, servicePriceInput;
    Button submitServiceBTN;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        serviceNameInput = findViewById(R.id.serviceNameInput);
        serviceDescriptionInput = findViewById(R.id.serviceDescriptionInput);
        servicePriceInput = findViewById(R.id.servicePriceInput);
        submitServiceBTN = findViewById(R.id.submitServiceBTN);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {

            finish();
        });

        dbHelper = new DatabaseHelper(this);

        submitServiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = serviceNameInput.getText().toString();
                String description = serviceDescriptionInput.getText().toString();
                String priceStr = servicePriceInput.getText().toString();

                if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(AddServiceActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    double price = Double.parseDouble(priceStr);
                    boolean success = addService(name, description, price);

                    if (success) {
                        Toast.makeText(AddServiceActivity.this, "Service added successfully", Toast.LENGTH_SHORT).show();
                        serviceNameInput.setText("");
                        serviceDescriptionInput.setText("");
                        servicePriceInput.setText("");
                    } else {
                        Toast.makeText(AddServiceActivity.this, "Error adding service", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean addService(String name, String description, double price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("service_name", name);
        values.put("description", description);
        values.put("price", price);

        long result = db.insert("services", null, values);
        db.close();
        return result != -1;
    }
}
