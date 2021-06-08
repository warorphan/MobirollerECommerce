package com.example.MobirollerEcommerceApp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.MobirollerEcommerceApp.R;
import com.example.MobirollerEcommerceApp.activities.ShowAllActivity;
import com.example.MobirollerEcommerceApp.adapters.CategoryAdapter;
import com.example.MobirollerEcommerceApp.adapters.NewProductsAdapter;
import com.example.MobirollerEcommerceApp.adapters.PopularProductAdapter;
import com.example.MobirollerEcommerceApp.models.CategoryModel;
import com.example.MobirollerEcommerceApp.models.NewProductsModel;
import com.example.MobirollerEcommerceApp.models.PopularProductsModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    TextView catShowAll, popularShowAll,newProductShowAll;



    //progressbar
    ProgressDialog progressDialog;
    LinearLayout linearLayout;


    DatabaseReference mReference;

    RecyclerView catRecyclerview, newProductRecyclerview,popularRecyclerview;

    //Category Recylerview
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    //New Product Recylerview
    NewProductsAdapter newProductsAdapter;
    List<NewProductsModel> newProductsModelList;

    //Popular products
    PopularProductAdapter popularProductAdapter;
    List<PopularProductsModel> popularProductsModelList;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //progress
        progressDialog = new ProgressDialog(getActivity());

        linearLayout = root.findViewById(R.id.home_layout);
        linearLayout.setVisibility(View.GONE);

        //New Products
        newProductRecyclerview = root.findViewById(R.id.new_product_rec);
        //Category
        catRecyclerview = root.findViewById(R.id.rec_category);
        //Popular
        popularRecyclerview = root.findViewById(R.id.popular_rec);

        //Show All
        catShowAll = root.findViewById(R.id.category_see_all);
        popularShowAll = root.findViewById(R.id.popular_see_all);
        newProductShowAll = root.findViewById(R.id.newProducts_see_all);

        catShowAll.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ShowAllActivity.class);
            startActivity(intent);
        });

        popularShowAll.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ShowAllActivity.class);
            startActivity(intent);
        });

        newProductShowAll.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ShowAllActivity.class);
            startActivity(intent);
        });


        //image slider
        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.banner1,"Discount On Shoes Items", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2, "Discount On Perfume", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3,"%70 OFF", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);

        //progress
        progressDialog.setTitle("Welcome To Mobiroller Challenge");
        progressDialog.setMessage("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        //Category
        catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        categoryModelList = new ArrayList<>();
        //categoryAdapter = new CategoryAdapter(getContext(),categoryModelList);
        categoryAdapter = new CategoryAdapter(getContext(),categoryModelList);
        catRecyclerview.setAdapter(categoryAdapter);


        mReference= FirebaseDatabase.getInstance().getReference().child("Category");
        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {



                CategoryModel categoryModel = snapshot.getValue(CategoryModel.class);
                categoryModelList.add(categoryModel);
                categoryAdapter.notifyDataSetChanged();
                linearLayout.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


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

                Toast.makeText(getActivity(),"Category Not Found",Toast.LENGTH_SHORT).show();

            }
        });




        //New Products
//        newProductRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        newProductRecyclerview.setLayoutManager(linearLayoutManager);
        newProductsModelList = new ArrayList<>();
        newProductsAdapter = new NewProductsAdapter(getContext(),newProductsModelList);
        newProductRecyclerview.setAdapter(newProductsAdapter);


        Query query= FirebaseDatabase.getInstance().getReference().child("Products").limitToLast(7);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                NewProductsModel newProductsModel = snapshot.getValue(NewProductsModel.class);
                newProductsModelList.add(newProductsModel);
                newProductsAdapter.notifyDataSetChanged();

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

                Toast.makeText(getActivity(),"New Product Not Found",Toast.LENGTH_SHORT).show();

            }
        });

        // Popular Products
        popularRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        popularProductsModelList = new ArrayList<>();
        popularProductAdapter = new PopularProductAdapter(getContext(),popularProductsModelList);
        popularRecyclerview.setAdapter(popularProductAdapter);

        mReference= FirebaseDatabase.getInstance().getReference().child("Products");
        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                PopularProductsModel popularProductsModel = snapshot.getValue(PopularProductsModel.class);
                popularProductsModelList.add(popularProductsModel);
                popularProductAdapter.notifyDataSetChanged();

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

                Toast.makeText(getActivity(),"Product List Not Found",Toast.LENGTH_SHORT).show();

            }
        });





        return root;
    }
}