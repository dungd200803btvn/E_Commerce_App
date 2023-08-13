package com.example.e_commerce.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "create table users(username text,email text,password text)";
        String query2 = "create table profile(fullname text,address text,phonenumber text,bankaccount text)";
        db.execSQL(query1);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void register(String username,String email,String password){
        ContentValues content = new ContentValues();
        content.put("username",username);
        content.put("email",email);
        content.put("password",password);
        SQLiteDatabase database = getWritableDatabase();
        database.insert("users",null,content);
    }
    public void InsertProfile(String fullname,String address,String phonenumber,String bankaccount){
        ContentValues content = new ContentValues();
        content.put("fullname",fullname);
        content.put("address",address);
        content.put("phonenumber",phonenumber);
        content.put("bankaccount",bankaccount);
        SQLiteDatabase database = getWritableDatabase();
        database.insert("profile",null,content);
    }

    public int login(String username,String password){
        int result =0;
        String[] str = new String[2];
        str[0] = username;
        str[1] = password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from users where username=? and password=?",str);
        if(c.moveToFirst()){
            result=1;
        }
        return result;
    }
    public int check_name(String fullname){
        int result =0;
        String[] str = new String[1];
        str[0] = fullname;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from profile where fullname=?",str);
        if(c.moveToFirst()){
            result=1;
        }
        return result;
    }
    public void resetPassWord(String username,String email,String newPass){
        String[] str = new String[2];
        str[0] = username;
        str[1] = email;
        ContentValues cv = new ContentValues();
        cv.put("password",newPass);
        SQLiteDatabase database = getWritableDatabase();
        database.update("users",cv,"username = ? and email =? ",str);
        database.close();
    }
    public void updateName(String newName,String oldname) {
        String[] str = new String[1];
        str[0] = oldname;
        ContentValues cv = new ContentValues();
        cv.put("fullname",newName);
        SQLiteDatabase database = getWritableDatabase();
        database.update("profile",cv,"fullname = ?",str);
    }
}
