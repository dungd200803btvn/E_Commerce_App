package com.example.e_commerce.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerce.databinding.ActivitySignUpChatAppBinding;
import com.example.e_commerce.model.Users;
import com.example.e_commerce.retrofit.ApiBanHang;
import com.example.e_commerce.retrofit.RetrofitClient;
import com.example.e_commerce.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpActivity_ChatApp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ActivitySignUpChatAppBinding binding;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpChatAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpActivity_ChatApp.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.txtUsername.getText().toString().isEmpty() && !binding.txtEmail.getText().toString().isEmpty()
                        && !binding.txtPass.getText().toString().isEmpty() && !binding.txtPhone.getText().toString().isEmpty()){
                    progressDialog.show();
                    postData();
                    mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        Users user = new Users(binding.txtUsername.getText().toString(),
                                                binding.txtEmail.getText().toString()
                                                ,binding.txtPass.getText().toString(),binding.txtPhone.getText().toString());
                                        String id = task.getResult().getUser().getUid();
                                        Paper.book().write("userid",  id);
                                        database.getReference().child("Users").child(id).setValue(user);
                                        Toast.makeText(SignUpActivity_ChatApp.this,"Sign up successful,Sign in now",Toast.LENGTH_SHORT).show();
                                        Intent it =  new Intent(SignUpActivity_ChatApp.this, SignInActivity.class);
                                        startActivity(it);
                                    }else{
                                        Toast.makeText(SignUpActivity_ChatApp.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    Toast.makeText(SignUpActivity_ChatApp.this,"Please enter your information",Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.txtHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SignUpActivity_ChatApp.this, SignInActivity.class);
                startActivity(it);
            }
        });
    }
    void postData(){
        apiBanHang = RetrofitClient.getInstance(Constants.BASE_URL).create(ApiBanHang.class);
        String username = binding.txtUsername.getText().toString().trim();
        String email = binding.txtEmail.getText().toString().trim();
        String pass = binding.txtPass.getText().toString().trim();
        String phonenumber = binding.txtPhone.getText().toString().trim();
        Paper.init(this);
        Paper.book().write("username",  username);
        Paper.book().write("email",  email);
        Paper.book().write("pass",  pass);
        Paper.book().write("phonenumber",  phonenumber);

        compositeDisposable.add(apiBanHang.Dangki(username,email,pass,phonenumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                Toast.makeText(this,"Cap nhat du lieu thanh cong len server",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(this,userModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }
}