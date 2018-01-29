package com.tpshop.mall.widget.swipetoloadlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.tpshop.mall.R;

public class SwipeLoadMoreFooterLayout extends FrameLayout implements SwipeLoadMoreTrigger, SwipeTrigger {

    public Context mContext;

    public SwipeLoadMoreFooterLayout(Context context) {
        this(context, null);
        init(context);
    }

    public SwipeLoadMoreFooterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public SwipeLoadMoreFooterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_footer_loading, this);
    }

    @Override
    public void onLoadMore() {
        Log.i("info", "onLoadMore");
    }

    @Override
    public void onPrepare() {
        Log.i("info", "onPrepare");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
    }

    @Override
    public void onRelease() {
        Log.i("info", "onRelease");
    }

    @Override
    public void onComplete() {
        Log.i("info", "onComplete");
    }

    @Override
    public void onReset() {
        Log.i("info", "onReset");
    }

}
