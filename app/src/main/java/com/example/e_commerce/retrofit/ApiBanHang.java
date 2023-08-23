package com.example.e_commerce.retrofit;

import com.example.e_commerce.WebAppadmin.GetCategoryModel;
import com.example.e_commerce.WebAppadmin.GetSpModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<GetCategoryModel> getloaiSp();

    @GET("getspchitiet.php")
    Observable<GetSpModel> getSpchitiet();

    @POST("filterbycategory.php")
    @FormUrlEncoded
    Observable<GetSpModel> getSpbyCategory(
            @Field("page") int page,
            @Field("loai") int loai
    );
}
