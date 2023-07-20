package com.example.e_commerce.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerce.database.Database;
import com.example.e_commerce.databinding.ActivityForgotPassBinding;

public class ForgotPassActivity extends AppCompatActivity {
ActivityForgotPassBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnResetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db =  new Database(getApplicationContext(),"ecommerce",null,1);
                String username = binding.username.getText().toString();
                String email = binding.email.getText().toString();
                String newPassword = binding.newPassword.getText().toString();
                String cfnewPassword = binding.Cfpassword.getText().toString();
                if( username.length()==0 ||email.length()==0 ||newPassword.length()==0 ||cfnewPassword.length()==0 ){
                    Toast.makeText(getApplicationContext(),"Please fill detail data",Toast.LENGTH_SHORT).show();
                }else{
                    if(newPassword.compareTo(cfnewPassword)==0){
                        if(isValid(newPassword)){
                            db.resetPassWord(username,email,newPassword);
                            Toast.makeText(getApplicationContext(),"Changed password success,login again!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPassActivity.this, LoginActivity.class));
                        }else{
                            Toast.makeText(getApplicationContext(),"Password need have at least 8 character,have digit,letter and symbol letter",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Pass and Confirm pass didn't match",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static Boolean isValid(String pass){
        int f1=0,f2=0,f3=0;
        if(pass.length()<8){
            return false;
        }else{
            for(int i=0;i< pass.length();i++){
                if(Character.isLetter(pass.charAt(i))){
                    f1=1;
                }
            }

            for(int i=0;i< pass.length();i++){
                if(Character.isDigit(pass.charAt(i))){
                    f2=1;
                }
            }

            for(int i=0;i< pass.length();i++){
                char c = pass.charAt(i);
                if(c>=33 && c<=46 || c==64){
                    f3=1;
                }
            }
            if(f1==1 && f2==1 && f3==1){
                return true;
            }
            return false;
        }
    }
}