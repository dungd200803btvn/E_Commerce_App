package com.example.e_commerce.retrofit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    private static Retrofit instance;
    public static Retrofit getInstance(String Url){
        if(instance == null){
            instance = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Url)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();

        }
        return instance;
    }
}
