package com.example.e_commerce.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerce.R;
import com.example.e_commerce.databinding.ItemCartBinding;
import com.example.e_commerce.databinding.QuantityDialogBinding;
import com.example.e_commerce.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    Context context;
    ArrayList<Product> products;
    CartListener cartListener;
    Cart cart;
    public interface CartListener{
        public void onQuantityChanged();
    }
    public CartAdapter(Context context,ArrayList<Product> products,CartListener cartListener){
        this.context = context;
        this.products = products;
        this.cartListener = cartListener;
        cart = TinyCartHelper.getCart();
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
    Product product = products.get(position);
        Glide.with(context).load(product.getImage()).into(holder.binding.imageItemcart);
        holder.binding.nameProductCart.setText(product.getName());
        holder.binding.priceCart.setText("PKR "+product.getPrice());
        holder.binding.quantityCartItem.setText(product.getQuantity()+ "item(s)");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuantityDialogBinding quantityDialogBindingbinding = QuantityDialogBinding.inflate(LayoutInflater.from(context));
                AlertDialog alertDialog = new AlertDialog.Builder(context).setView(quantityDialogBindingbinding.getRoot()).create();
                quantityDialogBindingbinding.productName.setText(product.getName());
                quantityDialogBindingbinding.productStock.setText("Stock: "+product.getStock());
                quantityDialogBindingbinding.quantity.setText(String.valueOf(product.getQuantity()));
                int stock = product.getStock();


                quantityDialogBindingbinding.minusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantity =product.getQuantity();
                        if(quantity>1){
                            quantity--;
                            product.setQuantity(quantity);
                            quantityDialogBindingbinding.quantity.setText(String.valueOf(quantity));
                            notifyDataSetChanged();
                            cart.updateItem(product,product.getQuantity());
                            cartListener.onQuantityChanged();
                        }
                    }
                });
                quantityDialogBindingbinding.plusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantity =product.getQuantity();
                        quantity++;
                        if(quantity>product.getStock()){
                            Toast.makeText(context,"Max stock available"+product.getStock(),Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            product.setQuantity(quantity);
                            quantityDialogBindingbinding.quantity.setText(String.valueOf(quantity));
                            notifyDataSetChanged();
                            cart.updateItem(product,product.getQuantity());
                            cartListener.onQuantityChanged();
                        }
                    }
                });
                quantityDialogBindingbinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
               alertDialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return products.size();
    }
    public class CartViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView);
        }
    }
}
