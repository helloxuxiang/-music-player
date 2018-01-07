package com.example.administrator.androidproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class LoginUtils {
    //保存帐号密码到data.xml中
    public static boolean saveUserInfo(Context context,String username,String password){
        SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.commit();
        return true;
    }

    //从data.xml文件中获取存储的帐号、密码
    public static Map<String, String> getUserInfo(Context context){
        SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        String username=sp.getString("username",null);
        String password=sp.getString("pwd",null);
        Map<String, String> userMap=new HashMap<String, String>();
        userMap.put("username",username);
        userMap.put("password",password);
        return userMap;
    }
}
