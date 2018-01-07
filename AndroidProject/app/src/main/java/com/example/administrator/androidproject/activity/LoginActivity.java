package com.example.administrator.androidproject.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.androidproject.MySQLiteOpenHelper;
import com.example.administrator.androidproject.utils.LoginUtils;
import com.example.administrator.androidproject.R;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText etUserName;
    private EditText etPassword;
    private boolean loginSuccess=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName= ((EditText) findViewById(R.id.et_username));
        etPassword= ((EditText) findViewById(R.id.et_password));

        //取出帐号密码
        Map<String, String> userInfo= LoginUtils.getUserInfo(this);
        if(userInfo!=null){
            //显示在界面上
            etUserName.setText(userInfo.get("username"));
            etPassword.setText(userInfo.get("password"));
        }
    }

    public void handleLogin(View v){
        //获取帐号密码
        String username=etUserName.getText().toString().trim();
        String password=etPassword.getText().toString().trim();
        //校验帐号密码
        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"请输入帐号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }

        MySQLiteOpenHelper mySQLiteOpenHelper=new MySQLiteOpenHelper(this);
        SQLiteDatabase db=mySQLiteOpenHelper.getWritableDatabase();
        //查询用户名是否已被使用
        Cursor cursor=db.rawQuery("select * from tb_user where username=? and password=?",new String[]{username,password});
        if(cursor.moveToNext()){
            String nickname=cursor.getString(2);
            Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
            loginSuccess=true;
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("nickname",nickname);
            setResult(1,intent);
            finish();
        }else{
            Toast.makeText(this,"帐号或密码错误",Toast.LENGTH_SHORT).show();
            loginSuccess=false;
            db.close();
        }

        if(loginSuccess==true){
            LoginUtils.saveUserInfo(this,username,password);
        }
    }

    public void handleZhuce(View v){
        Intent intent=new Intent(LoginActivity.this,ZhuceActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(resultCode == 1){
                if(requestCode == 1){
                    ((TextView) findViewById(R.id.et_username)).setText(data.getStringExtra("username"));
                    ((TextView) findViewById(R.id.et_password)).setText(data.getStringExtra("password"));
                }
            }
        }
    }
}
