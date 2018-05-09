package com.example.spirit.test830.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class QuickIndexBar extends View {

    private String[] indexArr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint paint;
    private int width;
    private float cellHeight;

    public QuickIndexBar(Context context) {
        super(context);
        init();
    }

    public QuickIndexBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickIndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);//设置文本底边边框的中心为绘制起点
        paint.setTextSize(15);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        cellHeight = getMeasuredHeight() * 1f / indexArr.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < indexArr.length; i++) {
            float x = width / 2;
            float y = cellHeight / 2 + getTextHeight(indexArr[i]) / 2 + i * cellHeight;

            paint.setColor(lastIndex == i ? Color.BLACK : Color.WHITE);

            canvas.drawText(indexArr[i], x, y, paint);
        }
    }

    private int getTextHeight(String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, 1, rect);
        return rect.height();
    }

    private int lastIndex = -1;//记录上次触摸事件的索引

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int index = (int) (y / cellHeight);//得到字母对应的索引
                if (index != lastIndex) {
//                    System.out.println(indexArr[index]);
                    if (index >= 0 && index < indexArr.length) {
                        if (letterListener != null) {
                            letterListener.onTouchLetter(indexArr[index]);
//                            System.out.println(indexArr[index]);
                        }
                    }
                }
                lastIndex = index;
                break;
            case MotionEvent.ACTION_UP:
                lastIndex = -1;//重置索引
                break;
        }

        invalidate();//引起重绘
        return true;
    }

    private OnTouchLetterListener letterListener;

    public void setLetterListener(OnTouchLetterListener letterListener) {
        this.letterListener = letterListener;
    }

    /**
     * 触摸字母的监听
     */
    public interface OnTouchLetterListener {
        void onTouchLetter(String word);
    }
}
