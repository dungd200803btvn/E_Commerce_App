package com.example.e_commerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerce.R;
import com.example.e_commerce.activity.ProductDetailActivity;
import com.example.e_commerce.databinding.ItemProductBinding;
import com.example.e_commerce.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   Context context;
   ArrayList<Product> products;
    private  static final int VIEW_TYPE_DATA = 0;
    private  static final int VIEW_TYPE_LOADING = 1;
   public ProductAdapter(Context context, ArrayList<Product> products){
       this.context = context;
       this.products = products;
   }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType == VIEW_TYPE_DATA ){
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
           return new ProductViewHolder(view);
       }else{
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass()== ProductViewHolder.class){
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            Product product = products.get(position);
            Glide.with(context).load(product.getImage()).into(productViewHolder.binding.image);
            productViewHolder.binding.label.setText(product.getName());

            int price = (int)product.getPrice() ;
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedNumber = formatter.format(price);

            productViewHolder.binding.price.setText(formattedNumber+" VND");
            productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, ProductDetailActivity.class);
                    it.putExtra("name",product.getName());
                    it.putExtra("image",product.getImage());
                    it.putExtra("price",product.getPrice());
                    it.putExtra("discount",product.getDiscount());
                    it.putExtra("stock",product.getStock());
                    it.putExtra("quantity",product.getQuantity());
                    it.putExtra("id",product.getId());
                    it.putExtra("status",product.getStatus());
                    context.startActivity(it);
                }
            });
        }else{
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder)holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }



    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public int getItemViewType(int position) {
        return products.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    public  class ProductViewHolder extends RecyclerView.ViewHolder{
        ItemProductBinding binding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }
    public  class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
}
