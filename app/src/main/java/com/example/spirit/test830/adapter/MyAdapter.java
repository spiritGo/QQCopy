package com.example.spirit.test830.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.spirit.test830.R;
import com.example.spirit.test830.bean.Friend;
import com.example.spirit.test830.utils.SpellUtil;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private ArrayList<Friend> list;
    private Context context;

    public MyAdapter(ArrayList<Friend> list, Context context) {
        this.list = list;
        this.context = context;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_friend, null);
            viewHolder = new ViewHolder();
            viewHolder.firstWord = convertView.findViewById(R.id.first_word);
            viewHolder.name = convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(list.get(position).getName());
        String currentWord = list.get(position).getPinYin().charAt(0) + "";
        if (position > 0) {
            String preWord = list.get(position - 1).getPinYin().charAt(0) + "";

            if (preWord.equals(currentWord)) {
                viewHolder.firstWord.setVisibility(View.GONE);
            } else {
                viewHolder.firstWord.setVisibility(View.VISIBLE);
                viewHolder.firstWord.setText(currentWord);
            }
        } else {
            viewHolder.firstWord.setText(currentWord);
            viewHolder.firstWord.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView firstWord;
        TextView name;
    }
}
