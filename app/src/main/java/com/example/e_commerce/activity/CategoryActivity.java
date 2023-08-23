package com.example.e_commerce.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.example.e_commerce.adapter.ProductAdapter;
import com.example.e_commerce.databinding.ActivityCategoryBinding;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.retrofit.ApiBanHang;
import com.example.e_commerce.retrofit.RetrofitClient;
import com.example.e_commerce.utils.Constants;
import java.util.ArrayList;
import java.util.Objects;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoryActivity extends AppCompatActivity {
ActivityCategoryBinding binding;
ApiBanHang apiBanHang;
CompositeDisposable compositeDisposable = new CompositeDisposable();
    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> products2 = new ArrayList<>();
    ProductAdapter productAdapter;
    Boolean isLoading =false;
    Handler handler = new Handler();
    LinearLayoutManager linearLayoutManager;
    int catId;
    int page =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiBanHang = RetrofitClient.getInstance(Constants.BASE_URL).create(ApiBanHang.class);
        catId = getIntent().getIntExtra("catId",0);
        String categoryName = getIntent().getStringExtra("categoryName");
        Objects.requireNonNull(getSupportActionBar()).setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.productlist.setLayoutManager(linearLayoutManager);
        binding.productlist.setHasFixedSize(true);
        getData(page);
        addEventLoad();
    }
    private void addEventLoad() {
        binding.productlist.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isLoading){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == products.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }
    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                products.add(null);
                productAdapter.notifyItemInserted(products.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                products.remove(products.size()-1);
                productAdapter.notifyItemRemoved(products.size());
                page+=1;
                getData(page);
                productAdapter.notifyDataSetChanged();
                isLoading=false;
            }
        },2000);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    void getData(int page){
        compositeDisposable.add(apiBanHang.getSpbyCategory(page,catId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        getSpModel -> {
                            if(getSpModel.isSuccess()){
                                if(productAdapter==null){
                                    products2 = (ArrayList<Product>) getSpModel.getResult();
                                    products.addAll(products2);
                                    productAdapter = new ProductAdapter(this,products);
                                    binding.productlist.setAdapter(productAdapter);
                                }else{
                                    int position = products.size()-1;
                                      int number_insert = getSpModel.getResult().size();
                                   for(int i=0;i<number_insert;i++){
                                        products.add(getSpModel.getResult().get(i));
                                }
                                   productAdapter.notifyItemRangeInserted(position,number_insert);
                                    }
                            }else{
                                Toast.makeText(this,"Het du lieu roi",Toast.LENGTH_LONG).show();
                                isLoading=true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(CategoryActivity.this,"Khong co ket noi voi server",Toast.LENGTH_LONG).show();
                        }
                )
        );
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}