package com.example.administrator.androidproject.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.androidproject.R;
import com.example.administrator.androidproject.MySQLiteOpenHelper;

public class ZhuceActivity extends AppCompatActivity {
    EditText zc_username;
    EditText zc_nickname;
    EditText et_zc_password;
    EditText et_zc_password2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);

        zc_username=(EditText)findViewById(R.id.et_zc_username);
        zc_nickname=(EditText)findViewById(R.id.et_zc_nickname);
        et_zc_password=(EditText)findViewById(R.id.et_zc_password);
        et_zc_password2=(EditText)findViewById(R.id.et_zc_password2);
    }

    /**
     *注册按钮点击事件处理
     */
    public void handleZhuce(View v){
        String username = zc_username.getText().toString().trim();
        String nickname = zc_nickname.getText().toString().trim();
        String password = et_zc_password.getText().toString().trim();
        String password2 = et_zc_password2.getText().toString().trim();

        //校验是否已输入完整信息
        if(inputCorrect(username,nickname,password,password2)==false){
            return;
        }
        //校验两次密码输入一致性
        if(pwdCorrect(password,password2)==false){
            return;
        }

        MySQLiteOpenHelper mySQLiteOpenHelper=new MySQLiteOpenHelper(this);
        SQLiteDatabase db=mySQLiteOpenHelper.getWritableDatabase();

        //查询用户名是否已被使用
        Cursor cursor=db.rawQuery("select * from tb_user where username=?",new String[]{username});
        if(cursor.moveToNext()){
            Toast.makeText(this,"用户已存在！",Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        db.execSQL("insert into tb_user (username,nickname,password) values(?,?,?)",new Object[]{username,nickname,password});//插入tb_user表
        Toast.makeText(this,"注册成功！",Toast.LENGTH_SHORT).show();
        db.close();

        //跳转回登录页面，将帐号和密码作为数据返回
        Intent intent=new Intent(ZhuceActivity.this,LoginActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("password",password);
        setResult(1,intent);
        finish();
    }



    /**
     * 校验两次密码输入一致性
     */
    public boolean pwdCorrect(String password1,String password2){
        if(password1.equals(password2)){
            return true;
        }
        Toast.makeText(this,"两次密码输入不一致！",Toast.LENGTH_LONG).show();
        return false;
    }

    /**
     * 校验是否已输入完整信息
     */
    public boolean inputCorrect(String username,String nickname,String password,String password2){
        boolean result=false;
        if(username.isEmpty()){
            Toast.makeText(this,"请输入帐号！",Toast.LENGTH_LONG).show();
            result=false;
        }else if(nickname.isEmpty()){
            Toast.makeText(this,"请输入昵称！",Toast.LENGTH_LONG).show();
            result=false;
        }else if(password.isEmpty()){
            Toast.makeText(this,"请输入密码！",Toast.LENGTH_LONG).show();
            result=false;
        }else if(password2.isEmpty()){
            Toast.makeText(this,"请输入确认密码！",Toast.LENGTH_LONG).show();
            result=false;
        }
        result=true;
        return result;
    }
}
