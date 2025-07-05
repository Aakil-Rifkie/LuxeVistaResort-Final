package com.example.luxevistaresortfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.Calendar;

public class ServiceBookingConfirmationActivity extends AppCompatActivity {

    TextView serviceNameConfirm, serviceDescriptionConfirm, servicePriceConfirm;
    TextView bookingDate, checkoutDate;
    Button confirmBookingButton;
    DatabaseHelper dbHelper;
    int serviceId, userId;
    String serviceName, description;
    double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        serviceNameConfirm = findViewById(R.id.roomNameConfirm);
        serviceDescriptionConfirm = findViewById(R.id.roomDescriptionConfirm);
        servicePriceConfirm = findViewById(R.id.roomPriceConfirm);
        bookingDate = findViewById(R.id.checkInDate);
        checkoutDate = findViewById(R.id.checkOutDate);
        confirmBookingButton = findViewById(R.id.confirmBookingButton);

        dbHelper = new DatabaseHelper(this);

        serviceId = getIntent().getIntExtra("service_id", -1);
        serviceName = getIntent().getStringExtra("service_name");
        userId = getIntent().getIntExtra("user_id", -1);
        description = getIntent().getStringExtra("service_description");
        price = getIntent().getDoubleExtra("service_price", 0.0);

        serviceNameConfirm.setText(serviceName);
        serviceDescriptionConfirm.setText(description);
        servicePriceConfirm.setText("Price: $" + price);

        bookingDate.setOnClickListener(v -> showDatePicker(bookingDate));

        confirmBookingButton.setOnClickListener(v -> confirmBooking());
        checkoutDate.setOnClickListener(v -> showDatePicker(checkoutDate));

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

        DatePickerDialog datePickerDialog = new DatePickerDialog(ServiceBookingConfirmationActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    targetView.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void confirmBooking() {
        String date = bookingDate.getText().toString();

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.isServiceAvailable(serviceId, date)) {
            Toast.makeText(this, "Service unavailable for selected date", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.addServiceBooking(serviceId, userId, date);
        if (success) {
            Toast.makeText(this, "Service booked successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error booking service", Toast.LENGTH_SHORT).show();
        }
    }
}
