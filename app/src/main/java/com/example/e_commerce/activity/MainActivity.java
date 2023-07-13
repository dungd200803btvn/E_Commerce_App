package com.example.e_commerce.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import com.example.e_commerce.adapter.CategoryAdapter;
import com.example.e_commerce.adapter.ProductAdapter;
import com.example.e_commerce.databinding.ActivityMainBinding;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.model.Product;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

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
        binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/1688932518965.png","Some caption here"));
        binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/1688932262943.png","Some caption here"));
        binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/1688932518965.png","Some caption here"));
        binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/50%20off.png","Some caption here"));
    }

    void initcategories(){
        categories = new ArrayList<>();
        categories.add(new Category("sport","https://tutorials.mianasad.com/ecommerce/uploads/category/1688960897834.png","#fe438e","Some description",1));
        categories.add(new Category("sport","https://tutorials.mianasad.com/ecommerce/uploads/category/1688960897834.png","#18ab4e","Some description",1));
        categories.add(new Category("sport","https://tutorials.mianasad.com/ecommerce/uploads/category/1688960897834.png","#ff870e","Some description",1));
        categories.add(new Category("sport","https://tutorials.mianasad.com/ecommerce/uploads/category/1688960897834.png","#fb0504","Some description",1));
        categories.add(new Category("sport","https://tutorials.mianasad.com/ecommerce/uploads/category/1688960897834.png","#18ab4e","Some description",1));
        categories.add(new Category("sport","https://tutorials.mianasad.com/ecommerce/uploads/category/1688960897834.png","#18ab4e","Some description",1));
        categories.add(new Category("sport","https://tutorials.mianasad.com/ecommerce/uploads/category/1688960897834.png","#18ab4e","Some description",1));
        categoryAdapter = new CategoryAdapter(this,categories);
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }
    void initproducts(){
        products = new ArrayList<>();
        products.add(new Product("aaa","https://tutorials.mianasad.com/ecommerce/uploads/product/1688793889177.png","READY STOCK",12,12,1,1));
        products.add(new Product("aaa","https://tutorials.mianasad.com/ecommerce/uploads/product/1688793889177.png","READY STOCK",12,12,1,1));
        products.add(new Product("aaa","https://tutorials.mianasad.com/ecommerce/uploads/product/1688793889177.png","READY STOCK",12,12,1,1));
        products.add(new Product("aaa","https://tutorials.mianasad.com/ecommerce/uploads/product/1688793889177.png","READY STOCK",12,12,1,1));
        products.add(new Product("aaa","https://tutorials.mianasad.com/ecommerce/uploads/product/1688793889177.png","READY STOCK",12,12,1,1));
        products.add(new Product("aaa","https://tutorials.mianasad.com/ecommerce/uploads/product/1688793889177.png","READY STOCK",12,12,1,1));
        products.add(new Product("aaa","https://tutorials.mianasad.com/ecommerce/uploads/product/1688793889177.png","READY STOCK",12,12,1,1));
        products.add(new Product("aaa","https://tutorials.mianasad.com/ecommerce/uploads/product/1688793889177.png","READY STOCK",12,12,1,1));
        productAdapter = new ProductAdapter(this,products);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productlist.setAdapter(productAdapter);
        binding.productlist.setLayoutManager(layoutManager);
    }
}