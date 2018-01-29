package com.tpshop.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * item页底部的webview
 */
public class ItemWebView extends WebView {

    private int t;
    public float oldY;

    public ItemWebView(Context context) {
        super(context);
    }

    public ItemWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float Y = ev.getY();
                float Ys = Y - oldY;
                if (Ys > 0 && t == 0)            //滑动到顶部让父控件重新获得触摸事件
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                oldY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.t = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
