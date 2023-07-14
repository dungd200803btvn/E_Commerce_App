package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.e_commerce.R;
import com.example.e_commerce.databinding.ActivityProductDetailBinding;
import com.example.e_commerce.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity {
ActivityProductDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        Double price = getIntent().getDoubleExtra("price",0);
        Integer id = getIntent().getIntExtra("id",0);
        Glide.with(this).load(image).into(binding.productImage);
        getProductDetails(id);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    void getProductDetails(int id){
        RequestQueue requestQueue  = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCT_DETAILS_URL + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("success")){
                        JSONObject product = jsonObject.getJSONObject("product");
                        //String description = product.getString("description");
                        String description = "abalksnfanfnlkanfalnf";
                        binding.productDescription.setText(Html.fromHtml(description));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
requestQueue.add(stringRequest);

    }
}