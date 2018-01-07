package com.example.administrator.androidproject.activity;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.androidproject.MySQLiteOpenHelper;
import com.example.administrator.androidproject.R;
import com.example.administrator.androidproject.Song;
import com.example.administrator.androidproject.activity.LocalMusicFragment;
import com.example.administrator.androidproject.activity.MainActivity;
import com.example.administrator.androidproject.activity.ShowActivity;
import com.example.administrator.androidproject.utils.MusicUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/12/30.
 */

public class PlayListFragment extends Fragment{
    private ListView mPlayListView;
    private PlayList_MusicAdapter adapter;

    MainActivity mainActivity=(MainActivity) getActivity();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        mPlayListView = (ListView) view.findViewById(R.id.playlist_view);
        adapter = new PlayList_MusicAdapter(mainActivity,MainActivity.playList);
        mPlayListView.setAdapter(adapter);

        //给播放列表绑定Item点击事件
        mPlayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //修改当前播放歌曲的标识
                mainActivity.myService.currIndex=position;
                mainActivity.myService.playNewMusic(mainActivity.myService.currIndex);

                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    /**
     * 播放音乐列表适配器
     */
    public class PlayList_MusicAdapter extends BaseAdapter {
        private MySQLiteOpenHelper mySQLiteOpenHelper;

        private Context context;
        private List<Song> list;
        public PlayList_MusicAdapter(Context context, List<Song> list) {
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
            convertView=View.inflate(getActivity(),R.layout.playlist_view_item,null);
            if(position==mainActivity.myService.currIndex){
                LinearLayout lParent=(LinearLayout) convertView.findViewById(R.id.linearlayout_item);

                lParent.setBackgroundColor(Color.GRAY);
            }else {
                LinearLayout lParent=(LinearLayout) convertView.findViewById(R.id.linearlayout_item);

                lParent.setBackgroundColor(Color.WHITE);
            }
            ((TextView) convertView.findViewById(R.id.tv_list_no)).setText(position+1+""); //显示序号
            ((TextView) convertView.findViewById(R.id.tv_playlist_song)).setText(list.get(position).getSong()); //显示歌名
            ((TextView) convertView.findViewById(R.id.tv_playlist_singer_song)).setText(list.get(position).getSinger()+" - "+list.get(position).getSong());   //显示歌手-歌名
            //给播放列表Item的按钮绑定点击事件
            ImageButton addButton=(ImageButton)convertView.findViewById(R.id.img_delete);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mySQLiteOpenHelper=new MySQLiteOpenHelper(getActivity());
                    SQLiteDatabase db=mySQLiteOpenHelper.getWritableDatabase();
                    Cursor cursor=db.rawQuery("select * from tb_playlist limit ?,1",new String[]{position+""});
                    if(cursor.moveToNext()){
                        db.execSQL("delete from tb_playlist where id=?",new Object[]{cursor.getInt(0)});
                    }
                    db.close();
                    if(position == mainActivity.myService.currIndex){
                        mainActivity.myService.mediaPlayer.stop();
                        mainActivity.playList.remove(position);
                        if(mainActivity.playList.size()>0){
                            if(mainActivity.myService.currIndex==mainActivity.playList.size()){
                                mainActivity.myService.currIndex=0;
                            }
                            mainActivity.myService.playNewMusic(mainActivity.myService.currIndex);
                        }
                    }else if(position < mainActivity.myService.currIndex) {
                        Toast.makeText(getActivity(),"无法删除",Toast.LENGTH_SHORT).show();
                    }else{
                        mainActivity.playList.remove(position);
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
        public void Update(){
            notifyDataSetChanged();
        }
    }
}
