package com.example.MobirollerEcommerceApp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.MobirollerEcommerceApp.R;
import com.example.MobirollerEcommerceApp.adapters.ShowAllAdapter;

import com.example.MobirollerEcommerceApp.models.ShowAllModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;
import java.util.List;


public class ShowAllActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;
    TextView category_name;
    DatabaseReference mreference;
    ImageView img_no_products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        String type = getIntent().getStringExtra("type");


        category_name=findViewById(R.id.catgry_name);
        img_no_products=findViewById(R.id.image_no_found);
        recyclerView = findViewById(R.id.show_all_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, showAllModelList);
        recyclerView.setAdapter(showAllAdapter);


        if (type == null || type.isEmpty()) {
            mreference = FirebaseDatabase.getInstance().getReference().child("Products");
            category_name.setText("All Products");

            mreference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    img_no_products.setVisibility(View.GONE);

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

        if (type != null && type.equalsIgnoreCase(type)) {

            category_name.setText(type.substring(0, 1).toUpperCase()+type.substring(1).toLowerCase());

            Query query = FirebaseDatabase.getInstance().getReference().child("Products")
                    .orderByChild("type")
                    .equalTo(type);

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
    }
}