package com.example.e_commerce.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.e_commerce.R;
import com.example.e_commerce.adapter.CategoryAdapter;
import com.example.e_commerce.adapter.ProductAdapter;
import com.example.e_commerce.chatbot.MainActivityChatbot;
import com.example.e_commerce.databinding.ActivityMainBinding;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.retrofit.ApiBanHang;
import com.example.e_commerce.retrofit.RetrofitClient;
import com.example.e_commerce.utils.Constants;
import com.example.e_commerce.whatapp_chat.ChatMain2;
import com.mancj.materialsearchbar.MaterialSearchBar;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import java.util.ArrayList;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
 ActivityMainBinding binding;

CategoryAdapter categoryAdapter;
ProductAdapter productAdapter;
ArrayList<Category> categories;
    ArrayList<Category> categories2;
ArrayList<Product> products;
    ArrayList<Product> products2;
CompositeDisposable compositeDisposable = new CompositeDisposable();
    CompositeDisposable compositeDisposable2 = new CompositeDisposable();
ApiBanHang apiBanHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.searchBar.setHint("Search");
        binding.searchBar.setSpeechMode(false);
        apiBanHang = RetrofitClient.getInstance(Constants.BASE_URL).create(ApiBanHang.class);


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
        compositeDisposable2.add( Schedulers.io().scheduleDirect(() -> {
                    initcategories();
                    initproducts();
         //   binding.carousel.addData(new CarouselItem("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png","Sale phụ kiện"));
          //  binding.carousel.addData(new CarouselItem("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png","Lễ hội trả góp"));
         //   binding.carousel.addData(new CarouselItem("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg","Màn hình tràn"));
                })
        );

        binding.cartIcon.setOnClickListener(v -> {
            Intent it = new Intent(MainActivity.this, CartActivity.class);
            startActivity(it);
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
    void Anhxa(){

    }
    void initproducts(){
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this,products);
        getProduct2();
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productlist.setLayoutManager(layoutManager);
        binding.productlist.setAdapter(productAdapter);
    }

    void initcategories(){
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



    void getProduct2(){
        compositeDisposable.add(apiBanHang.getSpchitiet().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                getSpModel -> {
                        if(getSpModel.isSuccess()){
                            products2 = (ArrayList<Product>) getSpModel.getResult();
                            products.addAll(products2);
                            productAdapter.notifyDataSetChanged();

                        }
                }, throwable -> Toast.makeText(MainActivity.this,"Khong ket noi duoc voi server"+throwable.getMessage(),Toast.LENGTH_SHORT).show()
        ));
    }

/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable2.clear();
        compositeDisposable.clear();
    }*/
}