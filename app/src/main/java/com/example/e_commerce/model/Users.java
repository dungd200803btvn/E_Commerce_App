package com.example.e_commerce.model;

public class Users {
    String image,username,email,pass,userID,lastMessage,status;
    String sex,dateofbirth,phonenumber;
    public Users(){

    }

    public Users(String image, String username, String email, String pass, String userID, String lastMessage, String status, String sex, String dateofbirth, String phonenumber) {
        this.image = image;
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.userID = userID;
        this.lastMessage = lastMessage;
        this.status = status;
        this.sex = sex;
        this.dateofbirth = dateofbirth;
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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
