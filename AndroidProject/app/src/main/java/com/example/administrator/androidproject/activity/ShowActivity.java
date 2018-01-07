package com.example.administrator.androidproject.activity;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.androidproject.R;
import com.example.administrator.androidproject.service.MyService;

import java.text.SimpleDateFormat;

public class ShowActivity extends AppCompatActivity {
    private TextView songName,singer, musicCurrentTime, musicTotalTime;
    private SeekBar seekBar;

    private ImageButton imgBack,imgMode ,imgBtnPlayOrPause, imgBtnPrevious, imgBtnNext;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    public MyService myService;

    private ImageView rotate_imageView;
    private ObjectAnimator mAnimator=null;

    //通过 Handler 更新 UI 上的组件状态
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (myService.mediaPlayer != null) {
                songName.setText(MainActivity.playList.get(myService.currIndex).getSong());
                singer.setText("— "+MainActivity.playList.get(myService.currIndex).getSinger()+" —");
                if(myService.tag==false){
                    seekBar.setProgress(0);
                    seekBar.setMax(MainActivity.playList.get(myService.currIndex).getDuration());
                    musicCurrentTime.setText(time.format(0));
                    musicTotalTime.setText(time.format(MainActivity.playList.get(myService.currIndex).getDuration()));
                }else{
                    seekBar.setProgress(myService.mediaPlayer.getCurrentPosition());
                    seekBar.setMax(myService.mediaPlayer.getDuration());
                    musicCurrentTime.setText(time.format(myService.mediaPlayer.getCurrentPosition()));
                    musicTotalTime.setText(time.format(myService.mediaPlayer.getDuration()));
                }
                if(myService.PLAY_MODE==0){
                    imgMode.setImageDrawable(getResources().getDrawable(R.drawable.oneloop));
                }else if(myService.PLAY_MODE==1){
                    imgMode.setImageDrawable(getResources().getDrawable(R.drawable.order));
                }
                if(myService.mediaPlayer.isPlaying()){
                    imgBtnPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                }else{
                    imgBtnPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
                }
            }
            handler.postDelayed(runnable, 200);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        myService=MainActivity.myService;

        //对应控件
        songName = (TextView) findViewById(R.id.songName);
        singer=(TextView)findViewById(R.id.singer);
        imgBack = (ImageButton) findViewById(R.id.img_back);
        imgMode=(ImageButton) findViewById(R.id.img_mode);
        musicCurrentTime = (TextView) findViewById(R.id.MusicCurrentTime);
        musicTotalTime = (TextView) findViewById(R.id.MusicTotalTime);
        seekBar = (SeekBar) findViewById(R.id.MusicSeekBar);
        imgBtnPlayOrPause = (ImageButton) findViewById(R.id.ImgBtnPlayOrPause);
        imgBtnPrevious = (ImageButton) findViewById(R.id.ImgBtnPrevious);
        imgBtnNext = (ImageButton) findViewById(R.id.ImgBtnNext);

        handler.post(runnable);

        rotate_imageView = (ImageView) findViewById(R.id.Image);
        mAnimator = ObjectAnimator.ofFloat(rotate_imageView, "rotation", 0f, 360.0f);
        mAnimator.setDuration(10000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatCount(-1);

        mAnimator.start();
        if(!myService.mediaPlayer.isPlaying()){
            mAnimator.pause();
        }

        //播放暂停按钮  点击事件监听
        imgBtnPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myService.tag==false){
                    myService.playNewMusic(myService.currIndex);
                }else{
                    myService.playOrPauseMusic();
                }

                if(myService.mediaPlayer.isPlaying()){
                    mAnimator.resume();
                }else{
                    mAnimator.pause();
                }
            }
        });

        //返回按钮  点击事件监听
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShowActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //播放模式按钮 点击事件监听
        imgMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.PLAY_MODE=(++myService.PLAY_MODE)%2;
                if(myService.PLAY_MODE==0){
                    Toast.makeText(ShowActivity.this,"单曲循环",Toast.LENGTH_SHORT).show();
                }else if(myService.PLAY_MODE==1){
                    Toast.makeText(ShowActivity.this,"顺序播放",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //下一首按钮  点击事件监听
        imgBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.playNext();
            }
        });

        //上一首按钮  点击事件监听
        imgBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.playPrevious();
            }
        });

        //进度条控件状态改变 事件监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    myService.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}