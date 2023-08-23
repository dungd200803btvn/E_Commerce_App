package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.e_commerce.adapter.CartAdapter;
import com.example.e_commerce.databinding.ActivityCartBinding;
import com.example.e_commerce.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.math.BigInteger;

public class CartActivity extends AppCompatActivity {
ActivityCartBinding binding;
CartAdapter cartAdapter;
ArrayList<Product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        products = new ArrayList<>();

        Cart cart = TinyCartHelper.getCart();
            for(Map.Entry<Item, Integer> item: cart.getAllItemsWithQty().entrySet()){
                Product product = (Product) item.getKey();
                    int quantity = item.getValue();
                    product.setQuantity(quantity);
                    products.add(product);
            }
        cartAdapter =  new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                BigInteger price2 = cart.getTotalPrice().toBigInteger();
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(price2);
                binding.subtotal.setText(formattedNumber+ " VND");
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // ve duông ke phan chia giua cac item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        binding.cartlist.setLayoutManager(layoutManager);
        binding.cartlist.addItemDecoration(itemDecoration);
        binding.cartlist.setAdapter(cartAdapter);

        BigInteger price2 = cart.getTotalPrice().toBigInteger();
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(price2);
        binding.subtotal.setText(formattedNumber+ " VND");
        // add continue button handler
        binding.continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}