package com.example.spirit.test830;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.spirit.test830.manager.SwipeLayoutManager;
import com.example.spirit.test830.views.SwipeLayout;
import com.nineoldandroids.view.ViewHelper;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;

public class ThirdActivity extends Activity {

    private ListView lv_list;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        lv_list = findViewById(R.id.lv_list);
        arrayList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            arrayList.add("item --- " + i);
        }
        lv_list.setAdapter(new MyAdapter(arrayList));
        lv_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    SwipeLayoutManager.getSwipeLayoutManager().closeCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {

            }
        });
    }

    class MyAdapter extends BaseAdapter implements SwipeLayout.OnSwipeStateChangeListener {

        private ArrayList<String> list;

        public MyAdapter(ArrayList<String> list) {
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
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.layout_list_item,
                        null);
            }

            ViewHolder holder = ViewHolder.getViewHolder(convertView);
            holder.tv_name.setText(list.get(position));
            holder.swipeLayout.setTag(position);
            holder.swipeLayout.setOnSwipeStateChangeListener(this);
            return convertView;
        }

        @Override
        public void OnOpen(Object tag) {
            System.out.println(tag+"OnOpen");
        }

        @Override
        public void OnClose(Object tag) {
            System.out.println(tag+"OnClose");
        }
    }

    static class ViewHolder {
        private TextView tv_name, tv_delete;
        private SwipeLayout swipeLayout;

        public ViewHolder(View convertView) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_delete = convertView.findViewById(R.id.tv_delete);
            swipeLayout = convertView.findViewById(R.id.sl_swipe);
        }

        public static ViewHolder getViewHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
