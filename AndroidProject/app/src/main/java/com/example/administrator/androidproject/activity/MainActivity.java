package com.example.administrator.androidproject.activity;

import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.androidproject.MySQLiteOpenHelper;
import com.example.administrator.androidproject.Song;
import com.example.administrator.androidproject.adapter.LeftAdapter;
import com.example.administrator.androidproject.R;
import com.example.administrator.androidproject.service.MyService;
import com.example.administrator.androidproject.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String UserName="";
    public static String NickName="";

    public static List<Song> localList;
    public static List<Song> playList;

    private DrawerLayout mDrawerLayout = null;
    private ListView mLeftListView;
    private LeftAdapter leftAdapter;
    private String [] menus={"我的消息","定时关闭","我的工具","关于app"};
    private int[] menuIcons={R.drawable.news,R.drawable.clock,R.drawable.tools,R.drawable.about};
    private Button btn_GoLogin;
    private Button btn_Exit;
    private Button btn_Local;
    private Button btn_PlayList;
    private View local_line;
    private View playList_line;
    private TextView tv_Buttom_song;
    private TextView tv_Buttom_singer;
    private LinearLayout layout_Buttom;
    private ImageButton img_Buttom_Play;
    private LocalMusicFragment localMusicFragment;
    private PlayListFragment playListFragment;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    public static MyService myService;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = ((MyService.MyBinder) (service)).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    localMusicFragment=new LocalMusicFragment();
                    playListFragment=new PlayListFragment();
                    //显示本地列表
                    ShowFragment_localList();
                    //控制UI改变
                    handler.post(runnable);
                    break;
            }
        }
    };
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
                    if(playList.size()==0){
                        layout_Buttom.setVisibility(View.GONE);
                    } else{
                        tv_Buttom_song.setText(playList.get(myService.currIndex).getSong());
                        tv_Buttom_singer.setText(playList.get(myService.currIndex).getSinger());
                        layout_Buttom.setVisibility(View.VISIBLE);
                    }

                    if(myService.mediaPlayer!=null){
                        if(myService.mediaPlayer.isPlaying()){
                            img_Buttom_Play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                        }else{
                            img_Buttom_Play.setImageDrawable(getResources().getDrawable(R.drawable.play));
                        }
                    }
                    handler.postDelayed(runnable, 200);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!NickName.isEmpty()){
            (findViewById(R.id.btn_gologin)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_username)).setText(NickName);
        }
        //开启并绑定服务
        Intent intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);
        bindService(intent,serviceConnection , this.BIND_AUTO_CREATE);

        localList = new ArrayList<>();
        playList= new ArrayList<>();

        //新线程读取localList，playList,读取完成发送广播100
        new Thread(){
            @Override
            public void run() {
                //把扫描到的音乐赋值给localList
                localList = MusicUtils.getMusicData(MainActivity.this);
                //读取数据库赋值给playList
                mySQLiteOpenHelper=new MySQLiteOpenHelper(MainActivity.this);
                SQLiteDatabase db=mySQLiteOpenHelper.getWritableDatabase();
                Cursor cursor=db.rawQuery("select * from tb_playlist",null);
                int index=0;
                while(cursor.moveToNext()){
                    Song song=new Song(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5),cursor.getLong(6));
                    playList.add(index++,song);
                }
                db.close();
                if(localList.size()>0){
                    handler.sendEmptyMessage(100);
                }
            };
        }.start();

        mLeftListView=(ListView)findViewById(R.id.list_left);
        leftAdapter=new LeftAdapter(this,menus,menuIcons);
        mLeftListView.setAdapter(leftAdapter);

        btn_Exit=(Button)findViewById(R.id.btn_exit);
        btn_GoLogin=(Button)findViewById(R.id.btn_gologin);
        btn_Local=(Button)findViewById(R.id.btn_local);
        btn_PlayList=(Button)findViewById(R.id.btn_playList);
        local_line=findViewById(R.id.local_line);
        playList_line=findViewById(R.id.playlist_line);


        tv_Buttom_song=(TextView)findViewById(R.id.tv_bottom_song);
        tv_Buttom_singer=(TextView)findViewById(R.id.tv_bottom_singer);
        img_Buttom_Play=(ImageButton) findViewById(R.id.img_buttom_play);
        layout_Buttom=(LinearLayout) findViewById(R.id.layout_buttom);



        //按钮打开抽屉事件绑定
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imgBtn_leftListOpen);
        imageButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //退出按钮   点击事件绑定
        btn_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myService.mediaPlayer!=null){
                    myService.mediaPlayer.stop();
                    myService.mediaPlayer.release();
                    myService.mediaPlayer = null;
                }
                unbindService(serviceConnection);
                System.exit(0);
            }
        });

        //立即登录按钮  点击事件绑定
        btn_GoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(intent,1);
            }
        });

        //本地列表按钮   点击事件绑定
        btn_Local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playList_line.setVisibility(View.INVISIBLE);
                local_line.setVisibility(View.VISIBLE);
                ShowFragment_localList();
            }
        });

        //播放列表按钮  点击事件绑定
        btn_PlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playList_line.setVisibility(View.VISIBLE);
                local_line.setVisibility(View.INVISIBLE);
                ShowFragment_playList();
            }
        });

        //底部播放按钮   点击事件绑定
        img_Buttom_Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myService.tag==false){
                    myService.playNewMusic(myService.currIndex);
                }else{
                    myService.playOrPauseMusic();
                }
            }
        });

        //底部布局  点击事件绑定
        layout_Buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ShowActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
            }
        });
    }

    //显示本地列表fragment方法
    private void ShowFragment_localList(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_layout,localMusicFragment);
        transaction.commit();
    }
    //显示播放列表fragment方法
    private void ShowFragment_playList(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_layout,playListFragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(resultCode == 1){
                if(requestCode == 1){
                    ((Button) findViewById(R.id.btn_gologin)).setVisibility(View.GONE);
                    Log.i("登录用户：",data.getStringExtra("username"));
                    Log.i("昵称：",data.getStringExtra("nickname"));
                    ((TextView) findViewById(R.id.tv_username)).setText(data.getStringExtra("nickname"));
                    UserName=data.getStringExtra("username");
                    NickName=data.getStringExtra("nickname");
                }
            }
        }
    }
}
