package com.tpshop.mall.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 提供禁止滑动功能的自定义ViewPager
 */
public class NoScrollViewPager extends ViewPager {

    private boolean noScroll = false;
    private boolean noEffectChange = true;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    /**
     * 切换页面时是否有动画
     */
    public void setNoEffectChange(boolean noEffectChange) {
        this.noEffectChange = noEffectChange;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        boolean me = false;
        try {
            me = !noScroll && super.onInterceptTouchEvent(arg0);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return me;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, this.noEffectChange);
    }

}