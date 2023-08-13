package com.example.e_commerce.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerce.R;
import com.example.e_commerce.database.Database;
import com.example.e_commerce.databinding.ActivityProfileBinding;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
ActivityProfileBinding binding;
    SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);

    private static final int PICK_IMAGE_REQUEST = 1; // Mã yêu cầu trả về khi chọn ảnh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        String url = sharedPreferences.getString("image","");
        Uri uri = Uri.parse(url);

       binding.productImage.setImageURI(uri);







        String name = sharedPreferences.getString("username","");
        binding.username.setText(name);
        binding.buttonupdate.setOnClickListener(new View.OnClickListener() {
            Database db =  new Database(getApplicationContext(),"ecommerce",null,1);

            @Override
            public void onClick(View v) {
                String fullname = binding.editTextfullname.getText().toString();
                String address = binding.editTextaddress.getText().toString();
                String phone = binding.editTextPhone.getText().toString();
                String bank = binding.editTextBank.getText().toString();
                if(fullname.length()==0 || address.length()==0 || phone.length()==0 || bank.length()==0){
                    Toast.makeText(getApplicationContext(),"Please fill detail data",Toast.LENGTH_SHORT).show();
                }else{
                    db.InsertProfile(fullname,address,phone,bank);
                    Toast.makeText(getApplicationContext()," Insert infomation successfull",Toast.LENGTH_SHORT).show();
                  //  binding.editTextfullname.setFocusable(false);
                  //  binding.editTextaddress.setFocusable(false);
                  //  binding.editTextPhone.setFocusable(false);
                  //  binding.editTextBank.setFocusable(false);
                  //  binding.buttonupdate.setEnabled(false);
                }
            }
        });
        binding.fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showUpdateNameDialog();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Lấy URI của ảnh từ intent
            Uri selectedImageUri = data.getData();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("image",selectedImageUri.toString());
            editor.apply();

            // Đổi URI thành Bitmap (hoặc bạn có thể làm theo cách khác tùy chọn)
            Bitmap selectedBitmap = null;
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Hiển thị hình ảnh trong ImageView
            binding.productImage.setImageBitmap(selectedBitmap);

        }
    }
    private void showUpdateNameDialog() {
        Database db =  new Database(getApplicationContext(),"ecommerce",null,1);
        String fullname = binding.editTextfullname.getText().toString();
     //   int result = db.check_name(fullname);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update fullname");
        // Kiểm tra xem đã có tên trong database chưa

            builder.setMessage("Fullname current: " + fullname + "\nDo you want to update fullname?:");


        builder.setView(binding.editTextfullname);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newName =binding.editTextfullname.getText().toString().trim();
                if (!newName.isEmpty()) {
                    db.updateName(newName,fullname);
                    Toast.makeText(getApplicationContext(),"Update name successfull",Toast.LENGTH_SHORT).show();
                    binding.editTextfullname.setText(newName);
                    binding.editTextfullname.setFocusable(false);

                } else {
                    Toast.makeText(ProfileActivity.this, "Please enter  new fullname", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
}