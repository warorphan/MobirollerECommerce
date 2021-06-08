package com.example.MobirollerEcommerceApp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.MobirollerEcommerceApp.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    String userEmail,userPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }


    public void signIn(View view) {
        userEmail = email.getText().toString();
        userPassword = password.getText().toString();

        if(TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword) ) {
            Toast.makeText(this, "Please fill in all the blanks...", Toast.LENGTH_SHORT).show();
            return;
        }
        else {

            auth.signInWithEmailAndPassword(userEmail,userPassword)
                    .addOnCompleteListener(LoginActivity.this, task -> {

                        if (task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Email or Password is incorrect",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void signUp(View view) {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));

    }
}