package com.example.e_commerce.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerce.adapter.ChatRVAdapter;
import com.example.e_commerce.databinding.ActivityMainChatbotBinding;
import com.example.e_commerce.model.ChatModel;
import com.example.e_commerce.model.MsgModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivityChatbot extends AppCompatActivity {
ActivityMainChatbotBinding binding;
private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatModel> chatModelArrayList;
    private ChatRVAdapter chatRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainChatbotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chatModelArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatModelArrayList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.idRVChats.setLayoutManager(linearLayoutManager);
        binding.idRVChats.setAdapter(chatRVAdapter);
        binding.idFABSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.idEditMessage.getText().toString().isEmpty()){
                    Toast.makeText(MainActivityChatbot.this,"Please enter your message",Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(binding.idEditMessage.getText().toString());
                binding.idEditMessage.setText("");
            }
        });
    }
    private void getResponse(String message){
        chatModelArrayList.add(new ChatModel(message,USER_KEY));
        chatRVAdapter.notifyDataSetChanged();
        String url ="http://api.brainshop.ai/get?bid=177142&key=EWKuXpObv7e6gHWb&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModel> call = retrofitAPI.getMessage(url);
        call.enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                if(response.isSuccessful()){
                    MsgModel msgModel = response.body();
                    chatModelArrayList.add(new ChatModel(msgModel.getCnt(),BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {
                chatModelArrayList.add(new ChatModel("Please revert your question",BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();
            }
        });

    }
}