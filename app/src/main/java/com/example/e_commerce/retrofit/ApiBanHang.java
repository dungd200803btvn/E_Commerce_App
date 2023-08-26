package com.example.e_commerce.retrofit;

import com.example.e_commerce.WebAppadmin.DonhangModel;
import com.example.e_commerce.WebAppadmin.GetCategoryModel;
import com.example.e_commerce.WebAppadmin.GetSpModel;
import com.example.e_commerce.WebAppadmin.UserModel;

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

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> Dangki(
            @Field("username") String username,
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("phonenumber") String phonenumber
    );

    @POST("donhang.php")
    @FormUrlEncoded
    Observable<GetSpModel> createOrder(
            @Field("sdt") String sdt,
            @Field("email") String email,
            @Field("total") String total,
            @Field("userid") String userid,
            @Field("address") String address,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );
    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<GetSpModel> search(
            @Field("search") String search
    );
    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonhangModel> xemDonhang(
            @Field("userid") String userid
    );
}
