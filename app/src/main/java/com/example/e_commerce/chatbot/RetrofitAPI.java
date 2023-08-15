package com.example.e_commerce.chatbot;

import com.example.e_commerce.model.MsgModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET
    Call<MsgModel> getMessage(@Url String url);
}
