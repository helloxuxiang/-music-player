package com.example.administrator.androidproject.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.androidproject.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        /**
         * 3秒钟后，自动调转到主界面上
         */
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //创建跳转意图对象
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                //补充：结束欢迎界面
                finish();
            }
        }, 3 * 1000);

    }
}
