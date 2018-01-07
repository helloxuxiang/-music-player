package com.example.administrator.androidproject.utils;

/** 用来扫描本地音频的工具类
 * 类里面定义一个扫描本地音频的方法，其返回值是一个list集合，集合里面装的泛型就是Music类
 * Created by Administrator on 2017/12/23.
 */
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.administrator.androidproject.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicUtils {
    /**
     * 扫描系统里面的音频文件，返回一个list集合
    */
    public static List<Song> getMusicData(Context context) {
        List<Song> list = new ArrayList<Song>();

        // 媒体库查询语句
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setSong( cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)) ); //歌曲名
                song.setFilename( cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)) ); //文件名
                song.setSinger( cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) );      //歌手
                song.setPath( cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)) );          //路径
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));      //时长
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));             //大小

                Log.i("歌曲的名称",  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))  );
                Log.i("歌曲的歌手名",  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))  );
                Log.i("歌曲文件的名称",  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))  );
                Log.i("歌曲文件的全路径",  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))  );
                Log.i("歌曲的总播放时长",  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))  );
                Log.i("歌曲文件的大小",   cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))  );

                if (song.getSize() > 1000 * 800) {
                    // 分离出歌曲名和歌手
                    if (song.getSong().contains(" - ")) {
                        String[] str = song.getSong().split(" - ");
                        song.setSinger(str[0]);
                        song.setSong(str[1]);
                    } else {
                        song.setSong(song.getSong());
                    }
                    list.add(song);
                }
            }
            // 释放资源
            cursor.close();
        }
        return list;
    }

    /*用来格式化获取到的时间*/
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }
}
