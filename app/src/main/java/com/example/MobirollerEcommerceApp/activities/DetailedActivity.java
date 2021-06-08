package com.example.MobirollerEcommerceApp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.MobirollerEcommerceApp.R;
import com.example.MobirollerEcommerceApp.models.NewProductsModel;
import com.example.MobirollerEcommerceApp.models.PopularProductsModel;
import com.example.MobirollerEcommerceApp.models.ShowAllModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating,name,description,price,quantity,companyname;
    Button addToCart,buyNow;
    ImageView addItems,removeItems;
    RatingBar myRating;

    int totalQuantity = 1;
    double totalPrice = 0.00 ;
    double prcnew;

    //New Products
    NewProductsModel newProductsModel = null;

    //Popular Products
    PopularProductsModel popularProductsModel = null;

    //Show All
    ShowAllModel showAllModel = null;


    private DatabaseReference mreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        mreference= FirebaseDatabase.getInstance().getReference().child("New Product");

        final Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof NewProductsModel){
            newProductsModel = (NewProductsModel) obj;
        }
        else if (obj instanceof PopularProductsModel){
            popularProductsModel = (PopularProductsModel) obj;
        }
        else if (obj instanceof ShowAllModel) {
            showAllModel = (ShowAllModel) obj;
        }




        detailedImg = findViewById(R.id.detailed_img);
        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailed_name);
        rating = findViewById(R.id.rating);
        price = findViewById(R.id.detailed_price);
        description = findViewById(R.id.detailed_desc);
        companyname = findViewById(R.id.company_name);
        myRating=findViewById(R.id.my_rating);

        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);

        //New Products
        if (newProductsModel != null){
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            name.setText(newProductsModel.getName());
            rating.setText(newProductsModel.getRating());
            myRating.setRating(Float.parseFloat(newProductsModel.getRating()));
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            companyname.setText(newProductsModel.getCompany_name());




        }

        //Popular Products
        if (popularProductsModel != null){
            Glide.with(getApplicationContext()).load(popularProductsModel.getImg_url()).into(detailedImg);
            name.setText(popularProductsModel.getName());
            rating.setText(popularProductsModel.getRating());
            myRating.setRating(Float.parseFloat(popularProductsModel.getRating()));
            description.setText(popularProductsModel.getDescription());
            price.setText(String.valueOf(popularProductsModel.getPrice()));
            companyname.setText(popularProductsModel.getCompany_name());



        }
        //Show All Products
        if (showAllModel != null){
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            myRating.setRating(Float.parseFloat(showAllModel.getRating()));
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            companyname.setText(showAllModel.getCompany_name());



        }
        DecimalFormat df=new DecimalFormat("#.##");

        prcnew = Double.parseDouble(price.getText().toString());

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalQuantity<10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    totalPrice =  (prcnew * totalQuantity);
                    String dx=df.format(totalPrice);
                    price.setText(dx);
                }

            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));

                    totalPrice = (prcnew * totalQuantity);
                    String dx=df.format(totalPrice);
                    price.setText(dx);
                }

            }
        });

    }


}