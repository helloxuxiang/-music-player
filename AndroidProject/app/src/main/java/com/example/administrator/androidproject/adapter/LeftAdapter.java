package com.example.administrator.androidproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.androidproject.R;

/**左侧抽屉列表适配器
 * Created by Administrator on 2017/12/30.
 */

public class LeftAdapter extends BaseAdapter {
    private Context context;
    private String [] menus;
    private int[] menuIcons;

    public LeftAdapter(Context context, String[] menus, int[] menuIcons) {
        this.context = context;
        this.menus = menus;
        this.menuIcons = menuIcons;
    }

    @Override
    public int getCount() {
        return menus.length;
    }
    @Override
    public Object getItem(int position) {
        return menus[position];
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView=View.inflate(context, R.layout.nv_left_listitem,null);
        ((ImageView) convertView.findViewById(R.id.img_menuIcon)).setImageResource(menuIcons[position]);
        ((TextView) convertView.findViewById(R.id.tv_menuItem)).setText(menus[position]);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        Toast.makeText(context,"0",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(context,"1",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(context,"2",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(context,"3",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        return convertView;
    }
}
