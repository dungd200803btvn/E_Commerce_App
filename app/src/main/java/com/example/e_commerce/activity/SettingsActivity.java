package com.example.e_commerce.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerce.R;
import com.example.e_commerce.databinding.ActivitySettingsBinding;
import com.example.e_commerce.login.SignInActivity;
import com.example.e_commerce.model.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
ActivitySettingsBinding binding;
FirebaseAuth firebaseAuth;
FirebaseDatabase database;
FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(it);
            }
        });
        binding.savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!binding.txtUsername.getText().toString().equals("") && !binding.txtabout.getText().toString().equals("")
               && !binding.txtsex.getText().toString().equals("") && !binding.txtdate.getText().toString().equals("") && !binding.txtphone.getText().toString().equals("")){
                   String username = binding.txtUsername.getText().toString();
                   String status = binding.txtabout.getText().toString();
                   String sex = binding.txtsex.getText().toString();
                   String dateofbirth = binding.txtdate.getText().toString();
                   String phonenumber = binding.txtphone.getText().toString();
                   HashMap<String, Object> objectsHashMap = new HashMap<>();
                   objectsHashMap.put("username",username);
                   objectsHashMap.put("status",status);
                   objectsHashMap.put("sex",sex);
                   objectsHashMap.put("dateofbirth",dateofbirth);
                   objectsHashMap.put("phonenumber",phonenumber);
                   database.getReference().child("Users").child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(objectsHashMap);
                   Toast.makeText(SettingsActivity.this,"Profile updated.",Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(SettingsActivity.this,"Enter information.",Toast.LENGTH_SHORT).show();
               }
            }
        });
        database.getReference().child("Users").child(Objects.requireNonNull(firebaseAuth.getUid()))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Users users = snapshot.getValue(Users.class);
                                Picasso.get().load(Objects.requireNonNull(users).getImage()).placeholder(R.drawable.avatar)
                                        .into(binding.profileImage);
                                binding.txtUsername.setText(users.getUsername());
                                binding.txtabout.setText(users.getStatus());
                                binding.txtsex.setText(users.getSex());
                                binding.txtdate.setText(users.getDateofbirth());
                                binding.txtphone.setText(users.getPhonenumber());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,25);
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent it1 = new Intent(SettingsActivity.this, SignInActivity.class);
                startActivity(it1);
                finish();
                Toast.makeText(SettingsActivity.this,"Log out successful",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            Uri sfile = data.getData();
            binding.profileImage.setImageURI(sfile);
             StorageReference reference = storage.getReference().child("profile_pic")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            reference.putFile(sfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users")
                                    .child(firebaseAuth.getUid())
                                    .child("image")
                                    .setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }
}