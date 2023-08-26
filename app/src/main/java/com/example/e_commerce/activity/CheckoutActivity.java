package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e_commerce.WebAppadmin.XemdonhangActivity;
import com.example.e_commerce.adapter.CartAdapter;
import com.example.e_commerce.databinding.ActivityCheckoutBinding;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.retrofit.ApiBanHang;
import com.example.e_commerce.retrofit.RetrofitClient;
import com.example.e_commerce.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CheckoutActivity extends AppCompatActivity {
    ActivityCheckoutBinding binding;
    BigDecimal totalcost ;
    final BigDecimal tax = BigDecimal.valueOf(1.1);
    Cart cart;
    ProgressDialog progressDialog;
    CartAdapter cartAdapter;
    ArrayList<Product> products;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    String formattedNumber2;
    BigInteger totalcost2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiBanHang = RetrofitClient.getInstance(Constants.BASE_URL).create(ApiBanHang.class);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");
        products = new ArrayList<>();

        cart = TinyCartHelper.getCart();
        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);
            products.add(product);
        }
        cartAdapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                BigInteger price2 = cart.getTotalPrice().toBigInteger();
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(price2);
                binding.subtotal.setText(formattedNumber+ " VND");
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // ve duông ke phan chia giua cac item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(cartAdapter);

        BigInteger price2 = cart.getTotalPrice().toBigInteger();
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(price2);
        binding.subtotal.setText(formattedNumber+ " VND");

       BigDecimal price3 = new BigDecimal(price2);
        totalcost = price3.multiply(tax);
        totalcost2 = totalcost.toBigInteger();
        DecimalFormat formatter2 = new DecimalFormat("#,###");
        formattedNumber2 = formatter2.format(totalcost2);
        binding.total.setText(formattedNumber2+ " VND");
        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkout();
            }
        });
        binding.btnXemdonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(CheckoutActivity.this, XemdonhangActivity.class);
                startActivity(it1);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    void checkout(){
        List<Product> product_gio_hang = new ArrayList<>();
        int sl=0;
        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);
            sl+= quantity;
           product_gio_hang.add(product);
        }
        if(!binding.phoneBox.getText().toString().isEmpty() && !binding.emailBox.getText().toString().isEmpty() &&
        !binding.nameBox.getText().toString().isEmpty() && !binding.addressBox.getText().toString().isEmpty()){
            String sdt = binding.phoneBox.getText().toString();
            String email = binding.emailBox.getText().toString();
            String total = totalcost2.toString();
            String userid = binding.nameBox.getText().toString();
            Paper.init(this);
            Paper.book().write("userid",  userid);
            String address = binding.addressBox.getText().toString();
            Gson gson = new GsonBuilder().setLenient().create();
            String json = gson.toJson(product_gio_hang);
            compositeDisposable.add(apiBanHang.createOrder(sdt,email,total,userid,address,sl,json)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            getSpModel -> {
                                Toast.makeText(this,"Gui du lieu don hang len server thanh cong!",Toast.LENGTH_SHORT).show();

                            },
                            throwable -> {
                                Toast.makeText(this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                    ));

            Intent it = new Intent(CheckoutActivity.this, PaymentRazoPay.class);
            it.putExtra("totalcost",formattedNumber2);
            it.putExtra("amount",totalcost);
            it.putExtra("name",binding.nameBox.getText().toString());
            startActivity(it);
        }else{
            Toast.makeText(this,"Enter information!",Toast.LENGTH_SHORT).show();
        }
    }

    void processOrder() {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject productOrder = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {
            productOrder.put("address", binding.addressBox.getText().toString());
            productOrder.put("buyer", binding.nameBox.getText().toString());
            productOrder.put("comment", binding.commentBox.getText().toString());
            productOrder.put("created_at", Calendar.getInstance().getTimeInMillis());
            productOrder.put("last_update", Calendar.getInstance().getTimeInMillis());
            productOrder.put("date_ship", Calendar.getInstance().getTimeInMillis());
            productOrder.put("email", binding.emailBox.getText().toString());
            productOrder.put("phone", binding.phoneBox.getText().toString());
            productOrder.put("serial", "cab8c1a4e4421a3b");
            productOrder.put("shipping", "");
            productOrder.put("shipping_location", "");
            productOrder.put("shipping_rate", "0.0");
            productOrder.put("status", "WAITING");
            productOrder.put("tax", tax);
            productOrder.put("total_fees", totalcost);

            JSONArray product_order_detail = new JSONArray();
            for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
                Product product = (Product) item.getKey();
                int quantity = item.getValue();
                product.setQuantity(quantity);
                JSONObject productObj = new JSONObject();
                productObj.put("amount", quantity);
                productObj.put("price_item", product.getPrice());
                productObj.put("product_id", product.getId());
                productObj.put("product_name", product.getName());
                product_order_detail.put(productObj);
            }
            dataObject.put("product_order", productOrder);
            dataObject.put("product_order_detail", product_order_detail);


        } catch (JSONException e) {

        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equals("success")){
                        Toast.makeText(CheckoutActivity.this,"Success order.",Toast.LENGTH_SHORT).show();
                        String order_number = response.getJSONObject("data").getString("code");
                        new AlertDialog.Builder(CheckoutActivity.this)
                                .setTitle("Order Successfull")
                                .setCancelable(false)
                                .setMessage("Your order name is "+order_number)
                                .setPositiveButton("Pay now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent it = new Intent(CheckoutActivity.this, PaymentRazoPay.class);
                                        it.putExtra("orderCode",order_number);
                                        String total = String.valueOf(totalcost);
                                        it.putExtra("totalcost",total);
                                        it.putExtra("amount",totalcost);
                                        it.putExtra("name",binding.nameBox.getText().toString());
                                        startActivity(it);
                                    }
                                }).show();
                    }else{
                        new AlertDialog.Builder(CheckoutActivity.this)
                                .setTitle("Order Failed")
                                .setCancelable(false)
                                .setMessage("Something were wrong,please try again")
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                        Toast.makeText(CheckoutActivity.this,"Fail order.",Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Security","secure_code");
                return headers;
            }
        };
requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}