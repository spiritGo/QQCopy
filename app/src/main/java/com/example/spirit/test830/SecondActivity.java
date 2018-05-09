package com.example.spirit.test830;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.spirit.test830.views.ParallaxListView;

public class SecondActivity extends Activity {

    private ParallaxListView lv_list;
    private String[] indexArr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        lv_list = findViewById(R.id.lv_list);
        lv_list.setOverScrollMode(ListView    .OVER_SCROLL_NEVER);//永远不显示顶部蓝色阴影
        //添加header
        View headerView = View.inflate(this, R.layout.layout_header, null);
        ImageView iv_img = headerView.findViewById(R.id.iv_img);
        lv_list.setImg(iv_img);
        lv_list.addHeaderView(headerView);
        lv_list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                indexArr) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
                    parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setGravity(Gravity.CENTER);
                return view;
            }
        });
    }
}
