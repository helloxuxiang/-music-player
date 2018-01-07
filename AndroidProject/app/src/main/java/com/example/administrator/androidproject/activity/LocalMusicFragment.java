package com.example.administrator.androidproject.activity;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.androidproject.MySQLiteOpenHelper;
import com.example.administrator.androidproject.R;
import com.example.administrator.androidproject.Song;
import com.example.administrator.androidproject.utils.MusicUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/12/30.
 */

public class LocalMusicFragment extends Fragment {
    private ListView mListView;
    private MusicAdapter adapter;

    MySQLiteOpenHelper mySQLiteOpenHelper;

    MainActivity mainActivity=(MainActivity) getActivity();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_localmusic, container, false);

        mListView = (ListView) view.findViewById(R.id.local_list_view);
        adapter = new MusicAdapter(mainActivity,MainActivity.localList);
        mListView.setAdapter(adapter);


        //给本地音乐列表绑定Item点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mySQLiteOpenHelper=new MySQLiteOpenHelper(getActivity());
                SQLiteDatabase db=mySQLiteOpenHelper.getWritableDatabase();
                //清空数据库中播放列表
                db.execSQL("delete from tb_playlist");
                //清空playList
                mainActivity.playList.clear();
                for(int i=0;i<mainActivity.localList.size();i++){
                    //循环插入歌曲到数据库播放列表
                    db.execSQL("insert into tb_playlist (song,filename,singer,path,duration,size)"+
                            "values(?,?,?,?,?,?)",new Object[]{mainActivity.localList.get(i).getSong(),mainActivity.localList.get(i).getFilename(),mainActivity.localList.get(i).getSinger(),
                            mainActivity.localList.get(i).getPath(),mainActivity.localList.get(i).getDuration(),mainActivity.localList.get(i).getSize()});
                    //循环添加歌曲到playList
                    mainActivity.playList.add(mainActivity.localList.get(i));
                }
                db.close();
                //修改当前播放歌曲的标识
                mainActivity.myService.currIndex=position;
                Log.i("当前播放歌曲索引值",mainActivity.myService.currIndex+"");
                mainActivity.myService.playNewMusic(mainActivity.myService.currIndex);

            }
        });
        return view;
    }

    /**
     * 本地音乐列表适配器
     */
    public class MusicAdapter extends BaseAdapter {
        private MySQLiteOpenHelper mySQLiteOpenHelper;

        private Context context;
        private List<Song> list;
        public MusicAdapter(Context context, List<Song> list) {
            this.context = context;
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }
        @Override
        public long getItemId(int position){
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView=View.inflate(getActivity(),R.layout.local_listview_item,null);
            ((TextView) convertView.findViewById(R.id.tv_song)).setText(list.get(position).getSong()); //显示歌名
            ((TextView) convertView.findViewById(R.id.tv_singer_song)).setText(list.get(position).getSinger()+" - "+list.get(position).getSong());   //显示歌手-歌名
            ((TextView) convertView.findViewById(R.id.tv_duration)).setText( MusicUtils.formatTime(list.get(position).getDuration())); //显示播放时长


            //给本地列表Item的按钮绑定点击事件
            ImageButton addButton=(ImageButton)convertView.findViewById(R.id.imgBtn_add);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.playList.add(MainActivity.playList.size(),list.get(position));

                    mySQLiteOpenHelper=new MySQLiteOpenHelper(getActivity());
                    SQLiteDatabase db=mySQLiteOpenHelper.getWritableDatabase();
                    db.execSQL("insert into tb_playlist (song,filename,singer,path,duration,size)"+
                            "values(?,?,?,?,?,?)",new Object[]{list.get(position).getSong(),list.get(position).getFilename(),list.get(position).getSinger(),
                            list.get(position).getPath(),list.get(position).getDuration(),list.get(position).getSize()});
                    db.close();
                    Toast.makeText(getActivity(),"已添加到播放列表",Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }
    }
}
