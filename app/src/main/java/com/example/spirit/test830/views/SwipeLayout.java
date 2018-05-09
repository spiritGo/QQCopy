package com.example.spirit.test830.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;

import com.example.spirit.test830.manager.SwipeLayoutManager;

public class SwipeLayout extends FrameLayout {

    private View deleteView;
    private View contentView;
    private int contentWidth;
    private int contentHeight;
    private int deleteWidth;
    private ViewDragHelper viewDragHelper;
    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == contentView || child == deleteView;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return deleteWidth;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (child == contentView) {
                if (left > 0) left = 0;
                if (left < -deleteWidth) left = -deleteWidth;
                System.out.println(left + ", " + deleteWidth);
            } else if (child == deleteView) {
                if (left > contentWidth) left = contentWidth;
                if (left < contentWidth - deleteWidth) left = contentWidth - deleteWidth;
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx,
                                          int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == contentView) {
                deleteView.layout(deleteView.getLeft() + dx, deleteView.getTop() + dy,
                        deleteView.getRight() + dx, deleteView.getBottom() + dy);
            } else if (changedView == deleteView) {
                contentView.layout(contentView.getLeft() + dx, contentView.getTop() + dy,
                        contentView.getRight() + dx, contentView.getBottom() + dy);
            }

            if (contentView.getLeft() == 0 && currentState != SwipeState.CLOSE) {
                currentState = SwipeState.CLOSE;

                if (listener != null) {
                    listener.OnClose(getTag());
                }

                SwipeLayoutManager.getSwipeLayoutManager().clearCurrentLayout();
            } else if (contentView.getLeft() == -deleteWidth && currentState != SwipeState.OPEN) {
                currentState = SwipeState.OPEN;
                if (listener != null) {
                    listener.OnOpen(getTag());
                }
                SwipeLayoutManager.getSwipeLayoutManager().setSwipeLayout(SwipeLayout.this);
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (contentView.getLeft() < -deleteWidth / 2) {
                //打开
                open();
            } else {
                //关闭
                close();
            }

//            requestDisallowInterceptTouchEvent(true);
        }
    };


    enum SwipeState {
        OPEN, CLOSE
    }

    private SwipeState currentState = SwipeState.CLOSE;

    public void close() {
        viewDragHelper.smoothSlideViewTo(contentView, 0, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    public void open() {
        viewDragHelper.smoothSlideViewTo(contentView, -deleteWidth, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        }
    }

    public SwipeLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        deleteView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentWidth = contentView.getMeasuredWidth();
        contentHeight = contentView.getMeasuredHeight();

        deleteWidth = deleteView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int l = getPaddingLeft();
        int t = getPaddingTop();
        contentView.layout(l, t, l + contentWidth, t + contentHeight);
        deleteView.layout(l + contentView.getRight(), t, l + contentView.getRight() + deleteWidth,
                t + contentHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);

        if (!SwipeLayoutManager.getSwipeLayoutManager().isShouldSwipe(this)) {
            SwipeLayoutManager.getSwipeLayoutManager().closeCurrentLayout();
            result = true;
        }

        return result;
    }

    private float downX = 0;
    private float downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!SwipeLayoutManager.getSwipeLayoutManager().isShouldSwipe(this)) {
            //先关闭已经打开的layout
            requestDisallowInterceptTouchEvent(true);
            return true;
        }


        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                float delatX = moveX - downX;//x方向移动的距离
                float delatY = moveY - downY;//y方向移动的距离

                if (Math.abs(delatX) > Math.abs(delatY)) {
                    //移动偏向水平
                    requestDisallowInterceptTouchEvent(true);
                } else {
                    //移动偏向垂直
                }

                downX = moveX;
                downY = moveY;
                break;
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
        }
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    private OnSwipeStateChangeListener listener;

    public void setOnSwipeStateChangeListener(OnSwipeStateChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSwipeStateChangeListener {
        void OnOpen(Object tag);

        void OnClose(Object tag);
    }
}
