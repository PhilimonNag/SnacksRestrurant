package com.philimonnag.snacksrestrurant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.philimonnag.snacksrestrurant.Models.Food;
import com.philimonnag.snacksrestrurant.R;
import com.philimonnag.snacksrestrurant.databinding.FoodItemBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    Context context;
    ArrayList<Food>arrayList;

    public FoodAdapter(Context context, ArrayList<Food> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.food_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FoodAdapter.ViewHolder holder, int position) {
        Food food=arrayList.get(position);
      holder.binding.FoodName.setText(food.getName());
      holder.binding.FoodDescription.setText(food.getDescription());
      holder.binding.FoodPrice.setText(" Rs."+ food.getPrice());
      Picasso.get().load(food.getFoodImg()).into(holder.binding.FoodImg);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FoodItemBinding binding;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=FoodItemBinding.bind(itemView);
        }
    }
}
