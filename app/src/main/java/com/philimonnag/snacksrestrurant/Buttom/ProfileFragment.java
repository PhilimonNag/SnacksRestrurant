package com.philimonnag.snacksrestrurant.Buttom;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.philimonnag.snacksrestrurant.Models.Restaurant;
import com.philimonnag.snacksrestrurant.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
private FragmentProfileBinding binding;
FirebaseUser firebaseUser;
Uri mProfileUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding=FragmentProfileBinding.inflate(inflater,container,false);
      View root= binding.getRoot();
       firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Restaurant").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Restaurant restaurant=snapshot.getValue(Restaurant.class);
                            binding.hotelName.setText(restaurant.getName());
                            binding.Adress.setText("Contact No : "+
                                    restaurant.getMobile()+"\n"
                                    +"Email Address : "+
                                    restaurant.getEmail()+"\n"+
                                    "Postal Address : "+
                                    restaurant.getPostalAddress());
                            binding.HotelDescription.setText(restaurant.getDescription());
                            if(restaurant.getRestaurantImg()!=null){
                                Picasso.get().load(restaurant.getRestaurantImg()).into(binding.HotelImg);
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        binding.HotelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picImg();
            }
        });
        binding.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            binding.Update.setVisibility(View.VISIBLE);
            binding.Edit.setVisibility(View.GONE);
            }
        });
        binding.Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mProfileUri==null){
                    Toast.makeText(getContext(), "Choose A Image For Your Hotel", Toast.LENGTH_SHORT).show();
                }else {
                    uploadImg();
                }
            }
        });
      return root;
    }

    private void uploadImg() {
        StorageReference reference= FirebaseStorage.getInstance().getReference("Hotel Image");
        UploadTask uploadTask= reference.putFile(mProfileUri);
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
                    Map<String,Object> map=new HashMap<>();
                    map.put("restaurantImg",url);
                    FirebaseDatabase.getInstance().getReference("Restaurant").
                            child(firebaseUser.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                           if (task.isSuccessful()){
                               Toast.makeText(getContext(), "Restaurant Image is Uploaded", Toast.LENGTH_SHORT).show();
                               binding.Edit.setVisibility(View.VISIBLE);
                               binding.Update.setVisibility(View.GONE);
                           }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.Edit.setVisibility(View.VISIBLE);
                            binding.Update.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

    }

    private void picImg() {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            //Image Uri will not be null for RESULT_OK
            assert data != null;
            mProfileUri = data.getData();
            Picasso.get().load(mProfileUri).into(binding.HotelImg);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}