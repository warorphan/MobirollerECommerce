package com.example.MobirollerEcommerceApp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.MobirollerEcommerceApp.R;
import com.example.MobirollerEcommerceApp.fragments.HomeFragment;
import com.example.MobirollerEcommerceApp.helpers.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    
    Fragment homefragment;
    FirebaseAuth auth;
    Toolbar toolbar;
    DatabaseReference ref;
    private Dialog dialog_chooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference().child("Users");


        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        homefragment = new HomeFragment();
        loadFragment(homefragment);
    }

    private void loadFragment(Fragment homeFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container,homeFragment);
        transaction.commit();
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem seller = menu.findItem(R.id.seller_account);
        if (auth.getCurrentUser()!=null){

            ref.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    seller.setVisible(!snapshot.child("userType").getValue().equals("Customer"));
                    Constant.USER_NAME=snapshot.child("name").getValue().toString();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return true;
    }

    //Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout){
            auth.signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        else if (id == R.id.seller_account){
            goChooser();
        }
        return true;
    }

    private void goChooser() {

        dialog_chooser = new Dialog(this);
        dialog_chooser.setContentView(R.layout.dialog_choose_folder);


        Button my_product = (Button) dialog_chooser.findViewById(R.id.btn_choose_kamera);
        Button add_product = (Button) dialog_chooser.findViewById(R.id.btn_choose_folder);

        my_product.setTextColor(getResources().getColor(R.color.material_blue_500));
        add_product.setTextColor(getResources().getColor(R.color.material_blue_500));


        my_product.setOnClickListener(View ->{
            startActivity(new Intent(MainActivity.this,MyProductsActivity.class));
            finish();
            dialog_chooser.dismiss();
        });

        add_product.setOnClickListener(View ->{
            startActivity(new Intent(MainActivity.this,SellerAddActivity.class));
            finish();
            dialog_chooser.dismiss();
        });

        Window window = dialog_chooser.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialog_chooser.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_chooser.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog_chooser.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mobiroller App");
        builder.setMessage("Do you want to quit?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
    }
}