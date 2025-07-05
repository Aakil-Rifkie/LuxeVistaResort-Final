package com.example.luxevistaresortfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ViewServicesActivity extends AppCompatActivity {

    ListView serviceListView;
    DatabaseHelper dbHelper;
    List<Service> serviceList;
    ServiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_services);

        serviceListView = findViewById(R.id.serviceListView);
        dbHelper = new DatabaseHelper(this);
        serviceList = new ArrayList<>();

        loadServices();

        adapter = new ServiceAdapter(this, serviceList);
        serviceListView.setAdapter(adapter);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {

            finish();
        });
    }

    private void loadServices() {
        Cursor cursor = dbHelper.getAllServices();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                double price = cursor.getDouble(3);

                serviceList.add(new Service(id, name, description, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
