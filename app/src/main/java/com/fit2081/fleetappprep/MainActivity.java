package com.fit2081.fleetappprep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fit2081.fleetappprep.LoginActivity;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText confPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button registerButton;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);
        confPassword = findViewById(R.id.conf_password_input);
        registerButton = findViewById(R.id.register_button);
        loginButton = findViewById(R.id.login_button);

        sharedPreferences = getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();

        registerButton.setOnClickListener(v -> {
            saveCredentials();
        });

        loginButton.setOnClickListener(v->{
            goLogin();
            finish();
        });

    }

    public void saveCredentials(){
        //Saves the login information to shared preferences

        String nameStr = username.getText().toString();
        String passStr = password.getText().toString();
        String confPassStr = confPassword.getText().toString();

        //Checks if the password or username is not empty
        //Also checks if the password is the same as the confirmed password input
        if(passStr.trim().length() > 0 && nameStr.trim().length() > 0 && passStr.equals(confPassStr)) {
            editor.putString(nameStr, passStr);
            editor.apply();
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            goLogin();
            finish();
        }
        else{
            Toast.makeText(this, "Invalid, fill all fields, and make sure passwords match", Toast.LENGTH_SHORT).show();
        }
    }

    public void goLogin(){
        //Transports user to the login page\
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}