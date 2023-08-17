package com.example.e_commerce.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.e_commerce.R;
import com.example.e_commerce.adapter.CategoryAdapter;
import com.example.e_commerce.adapter.ProductAdapter;
import com.example.e_commerce.chatbot.MainActivityChatbot;
import com.example.e_commerce.databinding.ActivityMainBinding;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.utils.Constants;
import com.example.e_commerce.whatapp_chat.ChatAppMainActivity;
import com.example.e_commerce.whatapp_chat.ChatMain2;
import com.mancj.materialsearchbar.MaterialSearchBar;
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
        binding.searchBar.setHint("Search");
        binding.searchBar.setSpeechMode(false);

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                String s = enabled ? "enabled" : "disabled";
                Toast.makeText(MainActivity.this, "Search " + s, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent it = new Intent(MainActivity.this, SearchActivity.class);
                it.putExtra("query",text.toString());
                startActivity(it);
            }
            @Override
            public void onButtonClicked(int buttonCode) {

                }
        });
        initcategories();
        initproducts();
        initslider();
        binding.cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, CartActivity.class);
                startActivity(it);
            }
        });
        binding.bottomNavigation.setSelectedItemId(R.id.bottom_home);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if(item.getItemId() ==R.id.bottom_home ){
                return true;
            }else if(item.getItemId()==R.id.bottom_chat){
                startActivity(new Intent(MainActivity.this, ChatMain2.class));
                overridePendingTransition(R.anim.silde_in_right, R.anim.slide_out_left);
                return true;
            }
            else if(item.getItemId()==R.id.bottom_chatbot){
                startActivity(new Intent(MainActivity.this, MainActivityChatbot.class));
                overridePendingTransition(R.anim.silde_in_right, R.anim.slide_out_left);
                return true;
            }
            else if(item.getItemId()==R.id.bottom_profile){
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                overridePendingTransition(R.anim.silde_in_right, R.anim.slide_out_left);
                return true;
            }
            else {
                return false;
            }
        });
    }
    void initproducts(){
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this,products);
        getRecentProducts();
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productlist.setLayoutManager(layoutManager);
        binding.productlist.setAdapter(productAdapter);
    }
    private void initslider() {
        getRecentOffer();
    }
    void initcategories(){
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this,categories);
        getCategories();
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
    void getRecentProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCTS_URL ;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray productsArray = object.getJSONArray("products");
                    for(int i =0; i< productsArray.length(); i++) {
                        JSONObject childObj = productsArray.getJSONObject(i);
                        Product product = new Product(
                                childObj.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + childObj.getString("image"),
                                childObj.getString("status"),
                                childObj.getDouble("price"),
                                childObj.getDouble("price_discount"),
                                childObj.getInt("stock"),
                                childObj.getInt("id")
                        );
                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });
        queue.add(request);
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