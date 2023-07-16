package com.example.e_commerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerce.R;
import com.example.e_commerce.activity.CategoryActivity;
import com.example.e_commerce.databinding.ItemCategoriesBinding;
import com.example.e_commerce.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHoder>{
    Context context;
    ArrayList<Category> categories;
    public CategoryAdapter(Context context ,ArrayList<Category> categories){
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHoder(LayoutInflater.from(context).inflate(R.layout.item_categories,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHoder holder, int position) {
      Category category = categories.get(position);
      holder.binding.productDescription.setText(Html.fromHtml(category.getName()));
        Glide.with(context).load(category.getIcon()).into(holder.binding.productImage);
        holder.binding.productImage.setBackgroundColor(Color.parseColor(category.getColor()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, CategoryActivity.class);
                it.putExtra("catId",category.getId());
                it.putExtra("categoryName",category.getName());
                context.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();

    }

    public class CategoryViewHoder extends RecyclerView.ViewHolder{
        ItemCategoriesBinding binding;
        public CategoryViewHoder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoriesBinding.bind(itemView);

        }
    }
}
