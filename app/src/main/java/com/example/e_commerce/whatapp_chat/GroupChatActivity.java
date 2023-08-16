package com.example.e_commerce.whatapp_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerce.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
ActivityGroupChatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(GroupChatActivity.this, ChatAppMainActivity.class);
                startActivity(it);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<ChatAppMessageModel> messageModels = new ArrayList<>();
        final String senderId = FirebaseAuth.getInstance().getUid();

        binding.username.setText("Group chat");

        final MessageAdapter messageAdapter = new MessageAdapter(messageModels,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.ChatRecyclerviewChatdetails.setAdapter(messageAdapter);
        binding.ChatRecyclerviewChatdetails.setLayoutManager(layoutManager);

        database.getReference().child("Group chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ChatAppMessageModel model = dataSnapshot.getValue(ChatAppMessageModel.class);
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
                final String message = binding.entermessage.getText().toString();
                ChatAppMessageModel model = new ChatAppMessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                binding.entermessage.setText("");
                database.getReference().child("Group chat").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(GroupChatActivity.this,"Message sended.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}