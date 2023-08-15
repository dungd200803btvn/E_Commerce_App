package com.example.e_commerce.model;

public class Users {
    String image,username,email,pass,userID,lastMessage,status;
    public Users(){

    }

    public Users(String image, String username, String email, String pass, String userID, String lastMessage, String status) {
        this.image = image;
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.userID = userID;
        this.lastMessage = lastMessage;
        this.status = status;
    }

    public Users(String username, String email, String pass) {
        this.username = username;
        this.email = email;
        this.pass = pass;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
