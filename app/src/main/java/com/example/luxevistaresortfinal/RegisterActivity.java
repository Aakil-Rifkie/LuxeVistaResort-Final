package com.example.luxevistaresortfinal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText emailRegister, passwordRegister, usernameRegister, ageRegister;
    Button registerButton;
    TextView loginText;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailRegister = findViewById(R.id.emailRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        usernameRegister = findViewById(R.id.usernameRegister);
        ageRegister = findViewById(R.id.ageRegister);
        registerButton = findViewById(R.id.registerButton);
        loginText = findViewById(R.id.loginText);

        String styledText = "No account? <font color='#00796B'><b><u>Register here</u></b></font>";
        loginText.setText(Html.fromHtml(styledText));

        dbHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetFieldColors();

                String email = emailRegister.getText().toString().trim();
                String password = passwordRegister.getText().toString().trim();
                String username = usernameRegister.getText().toString().trim();
                String ageText = ageRegister.getText().toString().trim();


                boolean valid = true;

                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailRegister.setBackgroundResource(R.drawable.input_error);
                    valid = false;
                }

                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    passwordRegister.setBackgroundResource(R.drawable.input_error);
                    valid = false;
                }

                if (TextUtils.isEmpty(username)) {
                    usernameRegister.setBackgroundResource(R.drawable.input_error);
                    valid = false;
                }

                int age = 0;
                try {
                    age = Integer.parseInt(ageText);
                    if (age < 18 || age > 100) {
                        ageRegister.setBackgroundResource(R.drawable.input_error);
                        valid = false;
                    }
                } catch (NumberFormatException e) {
                    ageRegister.setBackgroundResource(R.drawable.input_error);
                    valid = false;
                }

                if (!valid) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.emailExist(email)){
                    Toast.makeText(RegisterActivity.this, "This email is already registered", Toast.LENGTH_SHORT).show();
                    emailRegister.setBackgroundResource(R.drawable.input_error);
                    return;
                }

                boolean success = dbHelper.addUser(email, password, username, age);

                if (success) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void resetFieldColors() {
        emailRegister.setBackgroundColor(Color.TRANSPARENT);
        passwordRegister.setBackgroundColor(Color.TRANSPARENT);
        usernameRegister.setBackgroundColor(Color.TRANSPARENT);
        ageRegister.setBackgroundColor(Color.TRANSPARENT);
    }

}