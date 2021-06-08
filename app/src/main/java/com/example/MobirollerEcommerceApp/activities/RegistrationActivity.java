package com.example.MobirollerEcommerceApp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.MobirollerEcommerceApp.R;
import com.example.MobirollerEcommerceApp.helpers.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {

    EditText name,email,password;
    private FirebaseAuth auth;
    String userName,userEmail,userPassword;

    SharedPreferences sharedPreferences;
    DatabaseReference ref;

    private RadioGroup radioGroup;
    private RadioButton radioButtonCustomer, radioButtonSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //?
        //getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference();

        radioGroup=findViewById(R.id.radio_group);
        radioButtonCustomer=findViewById(R.id.radio1);
        radioButtonSeller=findViewById(R.id.radio2);

        if (auth.getCurrentUser() !=null){

            ref.child("Users").child(auth.getUid()).child("userType").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.getValue().equals("Seller")){
                        Constant.userType=1;
                    } else {
                        Constant.userType=0;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            finish();
        }

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        sharedPreferences = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("firstTime",true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioButtonSeller.isChecked()) {
                    name.setHint("Company Name");
                } else {
                    name.setHint("Your Name");
                }
            }
        });



        if (isFirstTime){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime",false);
            editor.commit();

            Intent intent = new Intent(RegistrationActivity.this,OnBoardingActivity.class);
            startActivity(intent);
            finish();

        }

    }

    public void signup(View view) {

        userName = name.getText().toString();
        userEmail = email.getText().toString();
        userPassword = password.getText().toString();

        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword) ) {
            Toast.makeText(this,"Please fill in all the blanks...",Toast.LENGTH_SHORT).show();
            return;

        }
        else {
            String userType="Customer";
            Constant.userType=0;
            if (radioButtonSeller.isChecked())
            {
               userType="Seller";
               Constant.userType=1;
            }
            String finalUserType = userType;
            auth.createUserWithEmailAndPassword(userEmail,userPassword)
                    .addOnCompleteListener(RegistrationActivity.this, task -> {

                        if (task.isSuccessful()){

                            ref.child("Users").child(auth.getUid()).child("email").setValue(userEmail);
                            ref.child("Users").child(auth.getUid()).child("name").setValue(userName);
                            ref.child("Users").child(auth.getUid()).child("email").setValue(userEmail);
                            ref.child("Users").child(auth.getUid()).child("id").setValue(auth.getUid());
                            ref.child("Users").child(auth.getUid()).child("userType").setValue(finalUserType);
                            Toast.makeText(RegistrationActivity.this,"Successfuly Register",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(RegistrationActivity.this,"Registration Failed" + task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    public void signin(View view) {
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));

    }
}