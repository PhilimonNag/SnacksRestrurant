package com.philimonnag.snacksrestrurant.Buttom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.philimonnag.snacksrestrurant.Models.Food;
import com.philimonnag.snacksrestrurant.databinding.FragmentAddFoodBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class AddFoodFragment extends Fragment {
private FragmentAddFoodBinding binding;
FirebaseUser firebaseUser;
Uri mProfileUri;
FirebaseStorage storage;
StorageReference reference;
Date date;
String FoodName;
String FoodDescription,price;
ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAddFoodBinding.inflate(inflater,container,false);
        View root=binding.getRoot();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        storage= FirebaseStorage.getInstance();
        date=new Date();
        pd=new ProgressDialog(getContext());
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        binding.FoodImg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               picImg();
           }
       });
    binding.AddFood.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FoodName=binding.DishName.getText().toString();
            FoodDescription=binding.FoodDescription.getText().toString();
            price=binding.foodPrice.getText().toString();
            if(TextUtils.isEmpty(FoodDescription)){
                binding.FoodDescription.setError("Food Description is Required");
            }else if(TextUtils.isEmpty(FoodName)){
                binding.DishName.setError("Food Name is Required");

            }else if(TextUtils.isEmpty(price)){
                binding.foodPrice.setError("Food Price is Required");

            }else if(mProfileUri==null){
                Toast.makeText(getContext(), "Choose a Food Image", Toast.LENGTH_SHORT).show();
            }else{
                pd.show();
                uploadData();
            }
        }
    });
        return root;
    }

    private void uploadData() {
        reference=storage.getReference().child("Food Item").child(date.getTime()+"");
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),mProfileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] fileInBytes = baos.toByteArray();
        UploadTask uploadTask = reference.putBytes(fileInBytes);
       Task<Uri>uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
           @Override
           public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
               if(!task.isSuccessful()){
                   throw task.getException();
               }
               return reference.getDownloadUrl();
           }
       }).addOnCompleteListener(new OnCompleteListener<Uri>() {
           @Override
           public void onComplete(@NonNull @NotNull Task<Uri> task) {
               if(task.isSuccessful()){
                      Uri downloadUrl=task.getResult();
                      String url=downloadUrl.toString();
                      Food food=new Food(FoodName,FoodDescription,url,price);
                   FirebaseDatabase.getInstance().
                           getReference("FoodItems")
                           .child(firebaseUser.getUid())
                           .push().setValue(food).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull @NotNull Task<Void> task) {
                           if(task.isSuccessful()){
                               pd.dismiss();
                               Toast.makeText(getContext(), "Food :"+FoodName+" is Successfully Added", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull @NotNull Exception e) {
                           pd.dismiss();
                           Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull @NotNull Exception e) {
               Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }

    private void picImg() {
        ImagePicker.with(this)
                .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){
            //Image Uri will not be null for RESULT_OK
            assert data != null;
            mProfileUri = data.getData();
            Picasso.get().load(mProfileUri).into(binding.FoodImg);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}