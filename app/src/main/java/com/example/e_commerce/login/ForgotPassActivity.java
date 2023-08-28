package com.example.e_commerce.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerce.database.Database;
import com.example.e_commerce.databinding.ActivityForgotPassBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {
ActivityForgotPassBinding binding;
FirebaseAuth mAuth;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = binding.edtForgotPasswordEmail.getText().toString().trim();
                if(!TextUtils.isEmpty(email)){
                        Resetpass();
                }else{
                    binding.edtForgotPasswordEmail.setError("Email field can't be empty");
                }
            }
        });
        binding.btnForgotPasswordBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
private void Resetpass(){
      //  binding.forgetPasswordProgressbar.setVisibility(View.VISIBLE);
      //  binding.btnReset.setVisibility(View.INVISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
        Toast.makeText(ForgotPassActivity.this,"Reset password link has been sent to your email",Toast.LENGTH_SHORT).show();
        Intent it = new Intent(ForgotPassActivity.this, SignInActivity.class);
        startActivity(it);
        finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPassActivity.this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
               // binding.forgetPasswordProgressbar.setVisibility(View.VISIBLE);
              //  binding.btnReset.setVisibility(View.INVISIBLE);

            }
        });
}

}