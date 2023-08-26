package com.example.e_commerce.WebAppadmin;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.e_commerce.databinding.ActivityXemdonhangBinding;
import com.example.e_commerce.retrofit.ApiBanHang;
import com.example.e_commerce.retrofit.RetrofitClient;
import com.example.e_commerce.utils.Constants;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemdonhangActivity extends AppCompatActivity {
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ActivityXemdonhangBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemdonhangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiBanHang = RetrofitClient.getInstance(Constants.BASE_URL).create(ApiBanHang.class);
        String userid = Paper.book().read("userid");
        Toast.makeText(this,userid,Toast.LENGTH_LONG).show();
        compositeDisposable.add(apiBanHang.xemDonhang(userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donhangModel -> {
                            Toast.makeText(this,donhangModel.getResult().get(0).getItem().get(0).getName(),Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Toast.makeText(this,throwable.getMessage(),Toast.LENGTH_LONG).show();

                        }
                ));
    }
}