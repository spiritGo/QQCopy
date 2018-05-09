package com.example.spirit.test830;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import com.example.spirit.test830.adapter.MyAdapter;
import com.example.spirit.test830.bean.Friend;
import com.example.spirit.test830.utils.SpellUtil;
import com.example.spirit.test830.views.QuickIndexBar;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Friend> friends;
    private ListView lv_list;
    private TextView tv_pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuickIndexBar quickIndexBar = findViewById(R.id.quickIndexBar);
        lv_list = findViewById(R.id.lv_list);
        tv_pg = findViewById(R.id.tv_pg);
        quickIndexBar.setLetterListener(new QuickIndexBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String word) {
                System.out.println(word);
                for (int i = 0; i < friends.size(); i++) {
                    String firstWord = friends.get(i).getPinYin().charAt(0) + "";
                    if (word.equals(firstWord)) {
                        lv_list.setSelection(i);
                        break;
                    }
                }
                showCurrentWord(word);
            }
        });

        friends = new ArrayList<>();
        fillList();
        Collections.sort(friends);
        lv_list.setAdapter(new MyAdapter(friends, this));

//        System.out.println(SpellUtil.getSpell("黑马"));
//        System.out.println(SpellUtil.getSpell("黑.,.。，马"));
        tvHide();
    }

    private void tvHide() {
        ViewHelper.setScaleX(tv_pg, 0);
        ViewHelper.setScaleY(tv_pg, 0);
    }

    private Handler handler = new Handler();
    private boolean isScale = false;

    private void showCurrentWord(String word) {
//        tv_pg.setVisibility(View.VISIBLE);
        if (!isScale) {
            isScale = true;
            ViewPropertyAnimator.animate(tv_pg).scaleX(1f).setDuration(450)
                    .setInterpolator(new OvershootInterpolator())
                    .start();
            ViewPropertyAnimator.animate(tv_pg).scaleY(1f).setDuration(450)
                    .setInterpolator(new OvershootInterpolator())
                    .start();
        }

        tv_pg.setText(word);

        //先移除之前的延时任务
        handler.removeCallbacksAndMessages(null);

        //延时隐藏textView
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                tv_pg.setVisibility(View.GONE);
//                tvHide();
                ViewPropertyAnimator.animate(tv_pg).scaleX(0).setDuration(450).start();
                ViewPropertyAnimator.animate(tv_pg).scaleY(0).setDuration(450).start();
                isScale = false;
            }
        }, 1500);
    }

    private void tvShow() {
        ViewHelper.setScaleX(tv_pg, 1);
        ViewHelper.setScaleY(tv_pg, 1);
    }

    private void fillList() {
        // 虚拟数据
        friends.add(new Friend("李伟"));
        friends.add(new Friend("张三"));
        friends.add(new Friend("阿三"));
        friends.add(new Friend("阿四"));
        friends.add(new Friend("段誉"));
        friends.add(new Friend("段正淳"));
        friends.add(new Friend("张三丰"));
        friends.add(new Friend("陈坤"));
        friends.add(new Friend("林俊杰1"));
        friends.add(new Friend("陈坤2"));
        friends.add(new Friend("王二a"));
        friends.add(new Friend("林俊杰a"));
        friends.add(new Friend("张四"));
        friends.add(new Friend("林俊杰"));
        friends.add(new Friend("王二"));
        friends.add(new Friend("王二b"));
        friends.add(new Friend("赵四"));
        friends.add(new Friend("杨坤"));
        friends.add(new Friend("赵子龙"));
        friends.add(new Friend("杨坤1"));
        friends.add(new Friend("李伟1"));
        friends.add(new Friend("宋江"));
        friends.add(new Friend("宋江1"));
        friends.add(new Friend("李伟3"));
    }
}
