package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.e_commerce.R;
import com.example.e_commerce.adapter.CategoryAdapter;
import com.example.e_commerce.adapter.ProductAdapter;
import com.example.e_commerce.chatbot.MainActivityChatbot;
import com.example.e_commerce.databinding.ActivityMain2Binding;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.retrofit.ApiBanHang;
import com.example.e_commerce.retrofit.RetrofitClient;
import com.example.e_commerce.utils.Constants;
import com.example.e_commerce.whatapp_chat.ChatMain2;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity2 extends AppCompatActivity {
ActivityMain2Binding binding;
ApiBanHang apiBanHang;
CompositeDisposable compositeDisposable = new CompositeDisposable();
    ArrayList<Product> products;
    ArrayList<Product> products2;
    ArrayList<Category> categories;
    ArrayList<Category> categories2;
    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;
int page=1;
int loai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiBanHang = RetrofitClient.getInstance(Constants.BASE_URL).create(ApiBanHang.class);
            binding.cartIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(MainActivity2.this, CartActivity.class);
                    startActivity(it);
                }
            });

        binding.bottomNavigation.setSelectedItemId(R.id.bottom_home);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if(item.getItemId() ==R.id.bottom_home ){
                return true;
            }else if(item.getItemId()==R.id.bottom_chat){
                startActivity(new Intent(MainActivity2.this, ChatMain2.class));
                overridePendingTransition(R.anim.silde_in_right, R.anim.slide_out_left);
                return true;
            }
            else if(item.getItemId()==R.id.bottom_chatbot){
                startActivity(new Intent(MainActivity2.this, MainActivityChatbot.class));
                overridePendingTransition(R.anim.silde_in_right, R.anim.slide_out_left);
                return true;
            }
            else if(item.getItemId()==R.id.bottom_profile){
                startActivity(new Intent(MainActivity2.this, SettingsActivity.class));
                overridePendingTransition(R.anim.silde_in_right, R.anim.slide_out_left);
                return true;
            }
            else {
                return false;
            }
        });
        initslider();
        initproducts();
        initcategory();

    }
    void initslider(){
        binding.carousel.addData(new CarouselItem("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png","Sale phụ kiện"));
         binding.carousel.addData(new CarouselItem("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png","Lễ hội trả góp"));
         binding.carousel.addData(new CarouselItem("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg","Màn hình tràn"));
    }
    void initproducts(){
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this,products);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productlist.setLayoutManager(layoutManager);
        binding.productlist.setAdapter(productAdapter);
        getProduct2();
    }
    void getProduct2(){
        compositeDisposable.add(apiBanHang.getSpchitiet().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                getSpModel -> {
                    if(getSpModel.isSuccess()){
                        products2 = (ArrayList<Product>) getSpModel.getResult();
                       products.addAll(products2);
                        productAdapter.notifyDataSetChanged();

                    }
                }, throwable -> Toast.makeText(MainActivity2.this,"Khong ket noi duoc voi server"+throwable.getMessage(),Toast.LENGTH_SHORT).show()
        ));
    }
    void initcategory(){
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this,categories);
        GetRecentCategory2();
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }
    private void GetRecentCategory2(){
        compositeDisposable.add(apiBanHang.getloaiSp().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                getSpModel -> {
                    if(getSpModel.isSuccess()){
                        categories2 = (ArrayList<Category>) getSpModel.getResult();
                        categories.addAll(categories2);
                        categoryAdapter.notifyDataSetChanged();
                    }
                }
        ));
    }


}