package com.philimonnag.snacksrestrurant.Buttom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philimonnag.snacksrestrurant.Adapters.FoodAdapter;
import com.philimonnag.snacksrestrurant.Models.Food;
import com.philimonnag.snacksrestrurant.databinding.FragmentMenuBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class MenuFragment extends Fragment {
private FragmentMenuBinding binding;
ArrayList<Food>arrayList;
FoodAdapter adapter;
FirebaseUser firebaseUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMenuBinding.inflate(inflater,container,false);
        View root=binding.getRoot();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        arrayList=new ArrayList<>();
        adapter=new FoodAdapter(getContext(),arrayList);
        binding.FoodRV.setHasFixedSize(true);
        binding.FoodRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        binding.FoodRV.setAdapter(adapter);
        loadFood();
        return root;
    }

    private void loadFood() {
        FirebaseDatabase.getInstance().getReference("FoodItems")
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            arrayList.clear();
                            for(DataSnapshot ds:snapshot.getChildren()){
                                Food food=ds.getValue(Food.class);
                                arrayList.add(food);
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error)  {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}