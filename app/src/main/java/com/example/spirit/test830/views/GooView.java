package com.example.spirit.test830.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.example.spirit.test830.utils.GeometryUtil;
import com.example.spirit.test830.utils.Utils;

public class GooView extends View {

    private Paint paint;
    private float dragRadius = 12f;
    private float stickyRadius = 12f;
    private PointF dragCenter = new PointF(200f, 120f);
    private PointF stickyCenter = new PointF(200f, 120f);
    private PointF[] stickyPoint = {new PointF(200f, 132f), new PointF(200f, 108f)};
    //private PointF[] dragPoint = {new PointF(120f, 132f), new PointF(120f, 108f)};
    private PointF[] dragPoint = {new PointF(200f, 132f), new PointF(200f, 108f)};
    private PointF controlPoint = new PointF(200f, 120f);

    public GooView(Context context) {
        super(context);
        init();
    }

    public GooView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GooView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
    }

    private double lineK;//斜率

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //让整体画布向上偏移
        canvas.translate(0, -Utils.getStatusBarHeight(getResources()));
        stickyRadius = getStickyRadius();


        float xOffset = dragCenter.x - stickyCenter.x;
        float yOffset = dragCenter.y - stickyCenter.y;
        if (xOffset != 0) {
            lineK = yOffset / xOffset;
        }
        dragPoint = GeometryUtil.getIntersectionPoints(dragCenter, dragRadius, lineK);
        stickyPoint = GeometryUtil.getIntersectionPoints(stickyCenter, stickyRadius, lineK);

        controlPoint = GeometryUtil.getPointByPercent(dragCenter, stickyCenter, 0.618f);

        // cx,cy圆心的坐标
        canvas.drawCircle(dragCenter.x, dragCenter.y, dragRadius, paint);

        if (!isDragOutOfRange) {
            canvas.drawCircle(stickyCenter.x, stickyCenter.y, stickyRadius, paint);
            //使用贝塞尔曲线连接
            Path path = new Path();
            path.moveTo(stickyPoint[0].x, stickyPoint[0].y);//设置起点

            //使用贝塞尔曲线连接：前两个坐标为控制点坐标
            path.quadTo(controlPoint.x, controlPoint.y, dragPoint[0].x, dragPoint[0].y);
            path.lineTo(dragPoint[1].x, dragPoint[1].y);
            path.quadTo(controlPoint.x, controlPoint.y, stickyPoint[1].x, stickyPoint[1].y);
            //path.close();//默认会闭合，可以不用调
            canvas.drawPath(path, paint);
        }

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(stickyCenter.x, stickyCenter.y, 80, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private boolean isDragOutOfRange = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDragOutOfRange = false;
                dragCenter.set(event.getRawX(), event.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                dragCenter.set(event.getRawX(), event.getRawY());
                if (GeometryUtil.getDistanceBetween2Points(dragCenter, stickyCenter) >
                        maxDistance) {
                    //超出范围，不再绘制贝塞尔曲线的部分
                    isDragOutOfRange = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (GeometryUtil.getDistanceBetween2Points(dragCenter, stickyCenter) >
                        maxDistance) {
                    dragCenter.set(stickyCenter);
                } else {
                    if (isDragOutOfRange) {
                        //曾经超出过范围
                        dragCenter.set(stickyCenter);
                    } else {

                        final PointF startPoint = new PointF(dragCenter.x, dragCenter.y);
                        //动画的形式回去
                        ValueAnimator animator = ObjectAnimator.ofFloat(1);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float animatedFraction = animation.getAnimatedFraction();
                                PointF pointByPercent = GeometryUtil.getPointByPercent
                                        (startPoint, stickyCenter, animatedFraction);
                                dragCenter.set(pointByPercent);
                                invalidate();
                            }
                        });

                        animator.setDuration(350);
                        animator.setInterpolator(new OvershootInterpolator());
                        animator.start();
                    }
                }
                break;
        }

        invalidate();
        return true;
    }

    private float maxDistance = 80;

    /**
     * 动态求出动态圆的半径
     */
    private float getStickyRadius() {
        float radius;
        float between2Points = GeometryUtil.getDistanceBetween2Points(dragCenter,
                stickyCenter);
        float fraction = between2Points / maxDistance;
        radius = GeometryUtil.evaluateValue(fraction, 12f, 4f);
        return radius;
    }
}
