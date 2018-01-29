package com.tpshop.mall.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tpshop.mall.R;

/**
 * Created by admin on 2017/6/17
 */
public class WebViewProgressBar extends View {

    private Paint paint;                                //进度条的画笔
    private int progress = 1;                           //进度默认为1
    private final static int HEIGHT = 5;                //进度条高度为5

    public WebViewProgressBar(Context context) {
        this(context, null);
    }

    public WebViewProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebViewProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    private void initPaint(Context context) {
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);                                             //填充方式为描边
        paint.setStrokeWidth(HEIGHT);                                                   //设置画笔的宽度
        paint.setAntiAlias(true);                                                       //抗锯齿
        paint.setDither(true);                                                          //使用抖动效果
        paint.setColor(context.getResources().getColor(R.color.light_red));             //画笔设置颜色
    }

    /**
     * 设置进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();           //刷新画笔
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth() * progress / 100, HEIGHT, paint);               //画矩形从(0.0)开始到(progress,height)的区域
    }

}
