package com.example.luxevistaresortfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class ViewRoomsActivity extends AppCompatActivity {

    ListView roomListView;
    TextView noRoomsMessage;
    DatabaseHelper dbHelper;
    List<Room> roomList;
    RoomAdapter adapter;
    String email;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rooms);

        Spinner sortSpinner = findViewById(R.id.sortSpinner);

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"Sort by", "Price: Low to High", "Price: High to Low"}
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);


        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    Collections.sort(roomList, Comparator.comparingDouble(Room::getPrice));
                } else if (position == 2) {
                    Collections.sort(roomList, (r1, r2) -> Double.compare(r2.getPrice(), r1.getPrice()));
                }

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        roomListView = findViewById(R.id.roomListView);
        noRoomsMessage = findViewById(R.id.noRoomsMessage);
        dbHelper = new DatabaseHelper(this);
        roomList = new ArrayList<>();

        email = getIntent().getStringExtra("user_email");

        loadRoomsFromDB();

        userId = dbHelper.getUserIdByEmail(email);
        adapter = new RoomAdapter(this, roomList, userId);
        roomListView.setAdapter(adapter);

        checkIfListIsEmpty();

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {

            finish();
        });
    }

    private void loadRoomsFromDB() {
        Cursor cursor = dbHelper.getAllRooms();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                double price = cursor.getDouble(3);

                roomList.add(new Room(id, name, description, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void checkIfListIsEmpty() {
        if (roomList.isEmpty()) {
            noRoomsMessage.setVisibility(View.VISIBLE);
            roomListView.setVisibility(View.GONE);
        } else {
            noRoomsMessage.setVisibility(View.GONE);
            roomListView.setVisibility(View.VISIBLE);
        }
    }
}
