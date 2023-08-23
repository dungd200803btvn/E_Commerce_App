package com.example.e_commerce.WebAppadmin;

import com.example.e_commerce.model.Product;

import java.util.List;

public class GetSpModel {
boolean success;
String message;
List<Product> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getResult() {
        return result;
    }

    public void setResult(List<Product> result) {
        this.result = result;
    }
}
