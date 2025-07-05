package com.example.luxevistaresortfinal;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    TextView registerText;
    EditText emailLogin, passwordLogin;

    Button loginButton;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.registerText);
        dbHelper = new DatabaseHelper(this);

        String styledText = "No account? <font color='#00796B'><b><u>Register here</u></b></font>";
        registerText.setText(Html.fromHtml(styledText));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String email =  emailLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String role = dbHelper.getUserRole(email, password);

                if (role == null) {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "User logged in succesfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                    intent.putExtra("user_email", email);
                    startActivity(intent);

                    finish();
                }
            }
        });


        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
