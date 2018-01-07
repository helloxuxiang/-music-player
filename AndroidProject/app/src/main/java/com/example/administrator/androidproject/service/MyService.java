package com.example.administrator.androidproject.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.example.administrator.androidproject.activity.MainActivity;

public class MyService extends Service {

    public MediaPlayer mediaPlayer;

    public int PLAY_MODE = 0;   //播放模式 0为单曲循环，1为顺序播放，2为随机播放

    public boolean tag = false;

    //当前播放的歌曲索引
    public int currIndex=0;

    public class MyBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
    @Override
    public void onDestroy() {
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    /*播放或暂停歌曲*/
    public void playOrPauseMusic(){
        if(mediaPlayer!=null){
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    switch (PLAY_MODE) {
//                        case 0:
//                            mediaPlayer.start();
//                            break;
//                        case 1:
//                            playNext();
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            });
            tag=true;
        }
    }
    //播放新音乐
    public void playNewMusic(final int currIndex){
        this.currIndex=currIndex;
        try {
            if(mediaPlayer!=null){
                mediaPlayer.stop();
            }
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(MainActivity.playList.get(currIndex).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    switch (PLAY_MODE) {
                        case 0:
                            mediaPlayer.start();
                            break;
                        case 1:
                            playNext();
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        tag=true;
    }

    /*下一首*/
    public void playNext(){
        currIndex=currIndex+1;
        if(currIndex>MainActivity.playList.size()-1){
            currIndex=0;
        }
        playNewMusic(currIndex);
    }

     /*上一首*/
    public void playPrevious(){
        currIndex=currIndex-1;
        if(currIndex< 0){
            currIndex=MainActivity.playList.size()-1;
        }
        playNewMusic(currIndex);
    }
}
