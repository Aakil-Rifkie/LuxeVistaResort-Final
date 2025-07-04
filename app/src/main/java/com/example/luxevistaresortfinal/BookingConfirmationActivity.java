package com.example.luxevistaresortfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.Calendar;

public class BookingConfirmationActivity extends AppCompatActivity {

    TextView roomNameConfirm, roomDescriptionConfirm, roomPriceConfirm;
    TextView checkInDate, checkOutDate;
    Button confirmBookingButton;
    DatabaseHelper dbHelper;
    int roomId, userId;
    String roomName, description;
    double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        roomNameConfirm = findViewById(R.id.roomNameConfirm);
        roomDescriptionConfirm = findViewById(R.id.roomDescriptionConfirm);
        roomPriceConfirm = findViewById(R.id.roomPriceConfirm);
        checkInDate = findViewById(R.id.checkInDate);
        checkOutDate = findViewById(R.id.checkOutDate);
        confirmBookingButton = findViewById(R.id.confirmBookingButton);

        dbHelper = new DatabaseHelper(this);

        roomId = getIntent().getIntExtra("room_id", -1);
        roomName = getIntent().getStringExtra("room_name");
        userId = getIntent().getIntExtra("user_id", -1);
        description = getIntent().getStringExtra("room_description");
        price = getIntent().getDoubleExtra("room_price", 0.0);

        roomNameConfirm.setText(roomName);
        roomDescriptionConfirm.setText(description);
        roomPriceConfirm.setText("Price: $" + price);

        checkInDate.setOnClickListener(v -> showDatePicker(checkInDate));
        checkOutDate.setOnClickListener(v -> showDatePicker(checkOutDate));

        confirmBookingButton.setOnClickListener(v -> confirmBooking());

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {

            finish();
        });
    }

    private void showDatePicker(TextView targetView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(BookingConfirmationActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" + String.format("%02d", (selectedMonth + 1)) + "-" + String.format("%02d", selectedDay);
                    targetView.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void confirmBooking() {
        String checkIn = checkInDate.getText().toString();
        String checkOut = checkOutDate.getText().toString();

        if (checkIn.isEmpty() || checkOut.isEmpty()) {
            Toast.makeText(this, "Please select both dates", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.isRoomAvailable(roomId, checkIn, checkOut)) {
            Toast.makeText(this, "Room unavailable for selected dates", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.addBooking(roomId, userId, checkIn, checkOut);
        if (success) {
            Toast.makeText(this, "Booking confirmed!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving booking", Toast.LENGTH_SHORT).show();
        }
    }
}
