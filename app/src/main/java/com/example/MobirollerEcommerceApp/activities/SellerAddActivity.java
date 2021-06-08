package com.example.MobirollerEcommerceApp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MobirollerEcommerceApp.R;
import com.example.MobirollerEcommerceApp.helpers.Constant;
import com.example.MobirollerEcommerceApp.models.ShowAllModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SellerAddActivity  extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    Spinner categorySpinner;

    List<String> arraySpinner = new ArrayList<String>();

    EditText price, desc, stock;
    TextView name;
    Button btn_apply;
    TextView err_txt,title;
    Button btn_image;

    String categoryname;

    int max,min;

    StorageReference storageReference;

    Uri imageUri;
    ImageView imageView;

    public static int PICK_IMAGE=1;

    Boolean image_=false;

    String uid_image;

    DatabaseReference ref;

    ProgressDialog loadingbar;
    String push_id;

    ShowAllModel model;
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add);

        categorySpinner=findViewById(R.id.spinner_category);
        categorySpinner.setOnItemSelectedListener(this);
        name=findViewById(R.id.item_name);
        price=findViewById(R.id.price);
        desc=findViewById(R.id.desc);
        stock=findViewById(R.id.stock);
        btn_apply=findViewById(R.id.button);
        err_txt=findViewById(R.id.error);
        btn_image=findViewById(R.id.add_image);
        imageView=findViewById(R.id.img);
        title=findViewById(R.id.title);

        final Object obj = getIntent().getSerializableExtra("edit");

        //Random rating
        Random r = new Random();
        max=5;
        min=3;
        float rat = min + r.nextFloat() * (max - min);
        double rating= Math.floor(rat* 10) / 10;


        arraySpinner.add("Select Category *");
        loadingbar=new ProgressDialog(this);

        ref= FirebaseDatabase.getInstance().getReference();

        ref.child("Category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String type_=snapshot.child("type").getValue().toString();
                arraySpinner.add(type_.substring(0, 1).toUpperCase()+type_.substring(1).toLowerCase());
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

        storageReference = FirebaseStorage.getInstance().getReference();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
               R.layout.spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        if (obj != null)
        {
            model = (ShowAllModel) obj;

            categorySpinner.setSelection(model.getType_id());
            name.setText(model.getName());
            desc.setText(model.getDescription());
            price.setText(model.getPrice());
            stock.setText(model.getStock());
            title.setText("Edit Product");
        }


        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (categorySpinner.getSelectedItemPosition()==0){
                    err_txt.setVisibility(View.VISIBLE);
                    err_txt.setText("Category is required.");
                    return;
                }

                if (name.getText().toString().equals("")){
                    err_txt.setVisibility(View.VISIBLE);
                    err_txt.setText("Name is required.");
                    return;
                }

                if (desc.getText().toString().equals("")){
                    err_txt.setVisibility(View.VISIBLE);
                    err_txt.setText("Description is required.");
                    return;
                }
                if (price.getText().toString().equals("")){
                    err_txt.setVisibility(View.VISIBLE);
                    err_txt.setText("Price is required.");
                    return;
                }
                if (stock.getText().toString().equals("")){
                    err_txt.setVisibility(View.VISIBLE);
                    err_txt.setText("Stock is required.");
                    return;
                }

                if (!image_){
                    err_txt.setVisibility(View.VISIBLE);
                    err_txt.setText("Image is required.");
                    return;
                }

                push_id = ref.child("Products").push().getKey();

                if (obj != null){
                    push_id=model.getItem_id();
                }

                ref.child("Products").child(push_id).child("name").setValue(name.getText().toString());
                ref.child("Products").child(push_id).child("company_name").setValue(Constant.USER_NAME);
                ref.child("Products").child(push_id).child("description").setValue(desc.getText().toString());
                ref.child("Products").child(push_id).child("item_id").setValue(push_id);
                ref.child("Products").child(push_id).child("price").setValue(price.getText().toString());
                ref.child("Products").child(push_id).child("stock").setValue(stock.getText().toString());
                ref.child("Products").child(push_id).child("type").setValue(categoryname.toLowerCase());
                ref.child("Products").child(push_id).child("type_id").setValue(categoryId);
                ref.child("Products").child(push_id).child("rating").setValue(String.valueOf(rating));

                uid_image=UUID.randomUUID().toString();
                uploadImage(uid_image);

                String k = getUrlAsync(uid_image);

                ref.child("Products").child(push_id).child("img_url").setValue(k);

            }
        });
    }
    private String getUrlAsync (String date){
        // Points to the root reference
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("images/" + date);

        final String[] uri = {""};
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                uri[0] =downloadUrl.toString();
            }

        });

        return uri[0];
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){

            if (data.getData()!=null)
            {
                imageUri = data.getData();
                image_=true;

                try {

                    // Setting image on image view using Bitmap
                    Bitmap bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(
                                    getContentResolver(),
                                    imageUri);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                image_=false;
            }
        }
        else{
            image_=false;
        }
    }

    private void uploadImage(String uid_image)
    {
        if (imageUri != null) {

            loadingbar.setTitle("Product Uploading");
            loadingbar.setMessage("Please wait..");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            final StorageReference filePath = storageReference.child("images/"+uid_image + ".jpg");

            UploadTask uploadTask = filePath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();

                    }

                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        final String mUri = downloadUri.toString();

                        ref.child("Products").child(push_id).child("img_url").setValue(mUri);
                        loadingbar.dismiss();

                        Toast.makeText(SellerAddActivity.this, "Succesfully!", Toast.LENGTH_SHORT).show();
                        Intent goMain=new Intent(SellerAddActivity.this,MainActivity.class);
                        startActivity(goMain);
                        finish();

                    } else {
                        Toast.makeText(SellerAddActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SellerAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();;

                }
            });
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        categoryname = arraySpinner.get(i);
        categoryId=i;
      //  Toast.makeText(getApplicationContext(),arraySpinner.get(i) , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}