package com.example.e_commerce.whatapp_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.e_commerce.R;
import com.example.e_commerce.activity.SettingsActivity;
import com.example.e_commerce.Fragment.FragmentAdapter;
import com.example.e_commerce.databinding.ActivityChatAppMainBinding;
import com.example.e_commerce.login.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ChatAppMainActivity extends AppCompatActivity {
ActivityChatAppMainBinding binding;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatAppMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        binding.viewPage.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPage);

    }

 /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chatapp,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.idsettings){
            Intent it = new Intent(ChatAppMainActivity.this, SettingsActivity.class);
            startActivity(it);

        }else if(item.getItemId()== R.id.idgroupchat){
            Intent intent = new Intent(ChatAppMainActivity.this, GroupChatActivity.class);
            startActivity(intent);

        }else if(item.getItemId()== R.id.idlogout){
            mAuth.signOut();
            Intent it = new Intent(ChatAppMainActivity.this, SignInActivity.class);
            startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }*/
}