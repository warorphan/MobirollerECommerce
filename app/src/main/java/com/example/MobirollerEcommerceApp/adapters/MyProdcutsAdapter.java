package com.example.MobirollerEcommerceApp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.MobirollerEcommerceApp.R;
import com.example.MobirollerEcommerceApp.activities.DetailedActivity;
import com.example.MobirollerEcommerceApp.activities.MyProductsActivity;
import com.example.MobirollerEcommerceApp.activities.SellerAddActivity;
import com.example.MobirollerEcommerceApp.helpers.Constant;
import com.example.MobirollerEcommerceApp.models.ShowAllModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class MyProdcutsAdapter extends RecyclerView.Adapter<MyProdcutsAdapter.ViewHolder> {

    private Context context;
    private List<ShowAllModel> list;
    private DatabaseReference ref;

    public MyProdcutsAdapter(Context context, List<ShowAllModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_products_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (list.get(position).getCompany_name().equals(Constant.USER_NAME))
        {
            Glide.with(context).load(list.get(position).getImg_url()).into(holder.mItemImage);
            holder.mName.setText(list.get(position).getName());
            holder.mCost.setText(list.get(position).getPrice());

            holder.img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SellerAddActivity.class);
                    intent.putExtra("edit",list.get(position));
                    context.startActivity(intent);
                }
            });

            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("Delete");
                    builder.setMessage("Do you really want to delete?");
                    builder.setNegativeButton("Hayır", null);
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ref= FirebaseDatabase.getInstance().getReference();
                            ref.child("Products").child(list.get(position).getItem_id()).removeValue();
                            Toast.makeText(context, "Succesfully delete!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MyProductsActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    builder.show();
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailedActivity.class);
                    intent.putExtra("detailed",list.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mItemImage , img_edit, img_delete;
        private TextView mCost;
        private TextView mName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemImage = itemView.findViewById(R.id.item_image);
            img_edit = itemView.findViewById(R.id.img_edit);
            img_delete = itemView.findViewById(R.id.img_delete);
            mCost = itemView.findViewById(R.id.item_cost);
            mName = itemView.findViewById(R.id.item_nam);

        }
    }
}
