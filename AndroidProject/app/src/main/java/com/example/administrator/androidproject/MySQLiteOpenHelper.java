package com.example.administrator.androidproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/12/29.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper{
    public MySQLiteOpenHelper(Context context) {
        super(context,"ccc.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_user (id integer primary key autoincrement," +
                "username varchar(20)," +
                "nickname varchar(20)," +
                "password varchar(20))");
        db.execSQL("create table tb_playlist (id integer primary key autoincrement," +
                "song varchar(20)," +
                "filename varchar(20)," +
                "singer varchar(20)," +
                "path varchar(20)," +
                "duration int," +
                "size long)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("MySQLiteOpenHelper","数据库版本变化了...");
    }
}
