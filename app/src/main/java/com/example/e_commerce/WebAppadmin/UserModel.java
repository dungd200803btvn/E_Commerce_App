package com.example.e_commerce.WebAppadmin;
import com.example.e_commerce.model.Users;

import java.util.List;

public class UserModel {
    boolean success;
    String message;
    List<Users> result;

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

    public List<Users> getResult() {
        return result;
    }

    public void setResult(List<Users> result) {
        this.result = result;
    }
}
