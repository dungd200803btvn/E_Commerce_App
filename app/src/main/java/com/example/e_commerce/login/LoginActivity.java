package com.example.e_commerce.login;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.e_commerce.activity.MainActivity;
import com.example.e_commerce.database.Database;
import com.example.e_commerce.databinding.ActivityLoginBinding;
public class LoginActivity extends AppCompatActivity {
ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.textviewRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  username = binding.username.getText().toString();
                String password = binding.password.getText().toString();
                Database db =  new Database(getApplicationContext(),"ecommerce",null,1);
                if(username.length()==0 || password.length()==0){
                    Toast.makeText(getApplicationContext(),"Please fill detail data",Toast.LENGTH_SHORT).show();
                }else{
                    if(db.login(username,password)==1){
                        Toast.makeText(getApplicationContext(),"Success Login",Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username",username);
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(),"Invalid Username or Password",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

      binding.textviewForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPassActivity.class));
            }
        });
    }
}