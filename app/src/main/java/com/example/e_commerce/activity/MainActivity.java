package com.example.e_commerce.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.e_commerce.adapter.CategoryAdapter;
import com.example.e_commerce.adapter.ProductAdapter;
import com.example.e_commerce.databinding.ActivityMainBinding;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.utils.Constants;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
CategoryAdapter categoryAdapter;
ProductAdapter productAdapter;
ArrayList<Category> categories;
ArrayList<Product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initcategories();
        initproducts();
        initslider();

    }

    private void initslider() {

     //   binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/50%20off.png","Some caption here"));
        getRecentOffer();
    }

    void initcategories(){
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this,categories);
        //getCategories();
        categories.add(new Category("Clothes","https://tutorials.mianasad.com/ecommerce/uploads/category/1689314293067.png","#4db151","color and beautiful",1));
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }
    void getCategories(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("success")){
                        JSONArray categoriesArray = jsonObject.getJSONArray("categories");
                        for(int i=0;i<categoriesArray.length();i++){
                            JSONObject object = categoriesArray.getJSONObject(i);
                            Category category = new Category(
                                    object.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL+  object.getString("icon"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id")
                            );
                            categories.add(category);
                        }
                            categoryAdapter.notifyDataSetChanged();
                    }else{

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
queue.add(stringRequest);
    }
    void getRecentProducts(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCTS_URL+"?count=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getString("status").equals("success")){
                    JSONArray array = jsonObject.getJSONArray("products");
                    for(int i=0;i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);

                        Product product = new Product(object.getString("name"),
                       Constants.PRODUCTS_IMAGE_URL+         object.getString("image"),
                                object.getString("status"),
                                object.getDouble("price"),
                                object.getDouble("price_discount"),
                                object.getInt("stock"),
                                object.getInt(" id")
                                );
                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }else{

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> {
        });
           requestQueue.add(stringRequest);
    }
    void initproducts(){
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this,products);
        products.add(new Product("Smart Watch","https://tutorials.mianasad.com/ecommerce/uploads/product/1689300402128.jpg","READY STOCK",2400,0,10,1));
        products.add(new Product("T-Shirt","https://tutorials.mianasad.com/ecommerce/uploads/product/1689314343600.png","READY STOCK",
                100,1,100,2));
       // getRecentProducts();
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productlist.setAdapter(productAdapter);
        binding.productlist.setLayoutManager(layoutManager);
    }
    void getRecentOffer(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("success")){
                        JSONArray array = object.getJSONArray("news_infos");
                        for(int i=0;i<array.length();i++){
                            JSONObject jsonObject = array.getJSONObject(i);
                            binding.carousel.addData(new CarouselItem(Constants.NEWS_IMAGE_URL+jsonObject.getString("image"),
                                    jsonObject.getString("title")));
                        }
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
queue.add(request);
    }
}