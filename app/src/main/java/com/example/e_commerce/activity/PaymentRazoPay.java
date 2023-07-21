package com.example.e_commerce.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerce.databinding.ActivityPaymentRazoPayBinding;
import com.example.e_commerce.model.CreateOrder;

import org.json.JSONObject;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentRazoPay extends AppCompatActivity  {
ActivityPaymentRazoPayBinding binding;
    private void IsLoading() {
        binding.zptranstoken.setVisibility(View.INVISIBLE);
        binding.txtTranstoken.setVisibility(View.INVISIBLE);
        binding.payBtn.setVisibility(View.INVISIBLE);
    }

    private void IsDone() {
        binding.zptranstoken.setVisibility(View.VISIBLE);
        binding.txtTranstoken.setVisibility(View.VISIBLE);
        binding.payBtn.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentRazoPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String orderCode = getIntent().getStringExtra("orderCode");
        String totalcost = getIntent().getStringExtra("totalcost");
        String name = getIntent().getStringExtra("name");
        binding.code.setText(orderCode);
        binding.name.setText(name);
        binding.totalAmt.setText(totalcost);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
      IsLoading();
            binding.btnCreateOrder.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    CreateOrder orderApi = new CreateOrder();

                    double doubleNumber = Double.parseDouble(totalcost);
                    int roundedNumber = (int) Math.floor(doubleNumber);
                    String amount = ""+roundedNumber;
                    try {
                        JSONObject data = orderApi.createOrder(amount);
                        String code = data.getString("return_code");
                        Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();
                            binding.zptranstoken.setText("zptranstoken");
                            binding.txtTranstoken.setText(data.getString("zp_trans_token"));
                           IsDone();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
       binding.payBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String token = binding.txtTranstoken.getText().toString();
               ZaloPaySDK.getInstance().payOrder(PaymentRazoPay.this, token, "demozpdk://app", new PayOrderListener() {
                   @Override
                   public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               new AlertDialog.Builder(PaymentRazoPay.this)
                                       .setTitle("Payment Success")
                                       .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                           }
                                       })
                                       .setNegativeButton("Cancel", null).show();
                           }

                       });
                       IsLoading();
                   }

                   @Override
                   public void onPaymentCanceled(String zpTransToken, String appTransID) {
                       new AlertDialog.Builder(PaymentRazoPay.this)
                               .setTitle("User Cancel Payment")
                               .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                   }
                               })
                               .setNegativeButton("Cancel", null).show();
                   }

                   @Override
                   public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                       new AlertDialog.Builder(PaymentRazoPay.this)
                               .setTitle("Payment Fail")
                               .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                   }
                               })
                               .setNegativeButton("Cancel", null).show();
                   }
               });
           }
       });
       binding.btnphuongthuckhac.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent it = new Intent(PaymentRazoPay.this, PaymentActivity.class);
               it.putExtra("orderCode",orderCode);
               startActivity(it);
           }
       });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}