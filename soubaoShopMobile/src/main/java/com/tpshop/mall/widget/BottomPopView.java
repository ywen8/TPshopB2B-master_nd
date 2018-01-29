package com.tpshop.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

/**
 * Created by wangqh on 2017-4-26
 */
public class BottomPopView extends RelativeLayout {

    public BottomPopView(Context context) {
        super(context);
    }

    public BottomPopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Context context = getContext();
        if (null != context) {
            int screenHeight = getScreenHeight(context);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(screenHeight * 2 / 3, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 获取屏幕高度
     */
    private int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

}
