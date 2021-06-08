package com.example.MobirollerEcommerceApp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.MobirollerEcommerceApp.R;
import com.example.MobirollerEcommerceApp.adapters.MyProdcutsAdapter;
import com.example.MobirollerEcommerceApp.helpers.Constant;
import com.example.MobirollerEcommerceApp.models.ShowAllModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class MyProductsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyProdcutsAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;
    TextView company_name;
    DatabaseReference mreference;
    ImageView img_no_products;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        company_name=findViewById(R.id.company_name);
        img_no_products=findViewById(R.id.image_no_found);
        recyclerView = findViewById(R.id.my_prod_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        showAllModelList = new ArrayList<>();
        showAllAdapter = new MyProdcutsAdapter(this, showAllModelList);
        recyclerView.setAdapter(showAllAdapter);


        company_name.setText(Constant.USER_NAME.substring(0, 1).toUpperCase()+Constant.USER_NAME.substring(1).toLowerCase());

        Query query = FirebaseDatabase.getInstance().getReference().child("Products")
                .orderByChild("company_name")
                .equalTo(Constant.USER_NAME);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot==null){
                    img_no_products.setVisibility(View.VISIBLE);
                }else {
                    img_no_products.setVisibility(View.GONE);
                }
                ShowAllModel showAllModel = snapshot.getValue(ShowAllModel.class);
                showAllModelList.add(showAllModel);
                showAllAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyProductsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}