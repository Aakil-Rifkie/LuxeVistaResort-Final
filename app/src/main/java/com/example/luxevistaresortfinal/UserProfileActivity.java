package com.example.luxevistaresortfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import java.util.Calendar;
import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    TextView usernameDisplay, emailDisplay, ageDisplay, travelStartDateDisplay, travelEndDateDisplay;
    EditText preferredRoomInput, preferredServiceInput;
    Button updateProfileButton;
    ListView bookingHistoryListView;
    DatabaseHelper dbHelper;
    String email;
    ArrayList<String> bookingHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        usernameDisplay = findViewById(R.id.usernameDisplay);
        emailDisplay = findViewById(R.id.emailDisplay);
        ageDisplay = findViewById(R.id.ageDisplay);
        travelStartDateDisplay = findViewById(R.id.travelStartDateDisplay);
        travelEndDateDisplay = findViewById(R.id.travelEndDateDisplay);
        preferredRoomInput = findViewById(R.id.preferredRoomInput);
        preferredServiceInput = findViewById(R.id.preferredServiceInput);
        updateProfileButton = findViewById(R.id.updateProfileButton);
        bookingHistoryListView = findViewById(R.id.bookingHistoryListView);

        dbHelper = new DatabaseHelper(this);
        email = getIntent().getStringExtra("user_email");

        loadProfile();
        loadBookingHistory();

        travelStartDateDisplay.setOnClickListener(v -> showDatePicker(travelStartDateDisplay));
        travelEndDateDisplay.setOnClickListener(v -> showDatePicker(travelEndDateDisplay));

        updateProfileButton.setOnClickListener(v -> updateProfile());

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {

            finish();
        });
    }

    private void loadProfile() {
        Cursor cursor = dbHelper.getUserByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            usernameDisplay.setText(cursor.getString(cursor.getColumnIndexOrThrow("username")));

            emailDisplay.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));

            ageDisplay.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("age"))));

            preferredRoomInput.setText(cursor.getString(cursor.getColumnIndexOrThrow("preferred_room")));

            preferredServiceInput.setText(cursor.getString(cursor.getColumnIndexOrThrow("preferred_service")));

            travelStartDateDisplay.setText(cursor.getString(cursor.getColumnIndexOrThrow("travel_start_date")));

            travelEndDateDisplay.setText(cursor.getString(cursor.getColumnIndexOrThrow("travel_end_date")));
            cursor.close();
        }
    }

    private void showDatePicker(TextView target) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            String date = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
            target.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateProfile() {
        String room = preferredRoomInput.getText().toString();
        String service = preferredServiceInput.getText().toString();
        String startDate = travelStartDateDisplay.getText().toString();
        String endDate = travelEndDateDisplay.getText().toString();

        boolean updated = dbHelper.updateUserProfile(email, room, service, startDate, endDate);

        if (updated) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBookingHistory() {
        int userId = dbHelper.getUserIdByEmail(email);
        bookingHistory = dbHelper.getUserBookingHistory(userId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingHistory);
        bookingHistoryListView.setAdapter(adapter);
    }
}
