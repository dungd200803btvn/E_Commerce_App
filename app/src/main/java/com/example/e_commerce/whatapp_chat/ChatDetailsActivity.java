package com.example.e_commerce.whatapp_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.e_commerce.R;
import com.example.e_commerce.databinding.ActivityChatDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailsActivity extends AppCompatActivity {
ActivityChatDetailsBinding binding;
FirebaseAuth firebaseAuth;
FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        final String senderId = firebaseAuth.getUid();
        String recieveId = getIntent().getStringExtra("userId");
        String username = getIntent().getStringExtra("username");
        String profilePic = getIntent().getStringExtra("profilePic");
        binding.username.setText(username);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileImage);
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ChatDetailsActivity.this, ChatAppMainActivity.class);
                startActivity(it);
            }
        });
        final ArrayList<ChatAppMessageModel> messageModels = new ArrayList<>();
        final MessageAdapter messageAdapter = new MessageAdapter(messageModels,this,recieveId);

        binding.ChatRecyclerviewChatdetails.setAdapter(messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.ChatRecyclerviewChatdetails.setLayoutManager(linearLayoutManager);
        final String senderRoom = senderId+recieveId;
        final String recieverRoom = recieveId+senderId;
        firebaseDatabase.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                    ChatAppMessageModel model = dataSnapshot1.getValue(ChatAppMessageModel.class);
                    model.setMessageId(dataSnapshot1.getKey());
                    messageModels.add(model);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sender_message = binding.entermessage.getText().toString();
                final ChatAppMessageModel model = new ChatAppMessageModel(senderId,sender_message);
                model.setTimestamp(new Date().getTime());
                binding.entermessage.setText("");
                firebaseDatabase.getReference().child("chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseDatabase.getReference().child("chats").child(recieverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });
    }
}