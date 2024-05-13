package com.fit2081.fleetappprep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    SharedPreferences sharedPreferences;
    Button registerButton;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.log_username_input);
        password = findViewById(R.id.log_pass_input);
        registerButton = findViewById(R.id.log_register_button);
        loginButton = findViewById(R.id.log_login_button);

        username.setText("");
        password.setText("");

        sharedPreferences = getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);

        registerButton.setOnClickListener(v -> {
            goRegister();
            finish();
        });

        loginButton.setOnClickListener(v->{
            login();

        });
    }

    public void login(){
        String name_str = username.getText().toString();
        String pass_str = password.getText().toString();

        //loads in the valid user details based on the username
        String auth_pass = sharedPreferences.getString(name_str, "");

        //Checks if the entered password, is the same as the one in shared preferences
        if(pass_str.equals(auth_pass) && pass_str.trim().length() > 0){
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Incorrect Login Details", Toast.LENGTH_SHORT).show();
        }
    }

    public void goRegister(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}