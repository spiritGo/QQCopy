package com.example.spirit.test830.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewAnimator;

public class ParallaxListView extends ListView {

    private ImageView img;
    private int orignalHeight;

    public ParallaxListView(Context context) {
        super(context);
    }

    public ParallaxListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int maxHeight;

    public void setImg(final ImageView img) {
        this.img = img;
        img.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                orignalHeight = img.getHeight();//img最初的高度
                int drawableHeight = img.getDrawable().getIntrinsicHeight();//获取图片的高度
                img.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                maxHeight = orignalHeight > drawableHeight ? orignalHeight * 2 : dp2Px(320);
            }
        });
    }


    private int dp2Px(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * 在listView滑动到头的时候执行，可以获取到继续滑动的距离和方向
     *
     * @param deltaX         继续滑动x方向的距离
     * @param deltaY         继续滑动y方向的巨鹿
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX x方向最大可以滚动的距离
     * @param maxOverScrollY y方向最大可以滚动的距离
     * @param isTouchEvent   true:手指拖动滑动     false:fling靠惯性滑动
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int
            scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean
                                           isTouchEvent) {
        if (deltaY < 0 && isTouchEvent) {
            //顶部滑动并且是手动拖动
            if (img != null) {
                int newHeight = img.getHeight() - deltaY/3;
                if (newHeight > maxHeight) newHeight = maxHeight;
                System.out.println(maxHeight);
                img.getLayoutParams().height = newHeight;
                img.requestLayout();//使布局参数生效
            }
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {

            ValueAnimator valueAnimator = ValueAnimator.ofInt(img.getHeight(), orignalHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //获取动画的值，设置给imageView
                    int animatedValue = (int) animation.getAnimatedValue();

                    img.getLayoutParams().height = animatedValue;
                    img.requestLayout();
                }
            });
            valueAnimator.setInterpolator(new OvershootInterpolator());
            valueAnimator.setDuration(350);
            valueAnimator.start();
        }
        return super.onTouchEvent(ev);
    }
}
