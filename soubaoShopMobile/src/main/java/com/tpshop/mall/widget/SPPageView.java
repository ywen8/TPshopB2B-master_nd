package com.tpshop.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.tpshop.mall.R;

/**
 * Created by admin on 2016/8/8
 */
public class SPPageView extends HorizontalScrollView {

    private boolean flag;
    private int mPageCount;                     //页面数量
    private int mBaseScrollX;                   //滑动基线,也就是点击并滑动之前的x值,以此值计算相对滑动距离
    private int mScreenWidth;
    private int mScreenHeight;
    private LinearLayout mContainer;
    private PageListener pageListener;

    public SPPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = Float.valueOf(context.getResources().getDimension(R.dimen.dp_300)).intValue();
    }

    public void setScreenHeight(int screenHeight) {
        this.mScreenHeight = screenHeight;
    }

    /**
     * 添加一个页面到最后
     */
    public void addPage(View page) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mScreenWidth, mScreenHeight);
        addPage(page, -1, params);
    }

    /**
     * 添加一个页面到最后
     */
    public void addPage(View page, ViewGroup.LayoutParams params) {
        addPage(page, -1, params);
    }

    /**
     * 添加一个页面
     */
    public void addPage(View page, int index, ViewGroup.LayoutParams params) {
        if (!flag) {
            mContainer = (LinearLayout) getChildAt(0);
            flag = true;
        }
        if (index == -1)
            mContainer.addView(page, params);
        else
            mContainer.addView(page, index, params);
        mPageCount++;
    }

    /**
     * 添加一个页面
     */
    public void addPage(View page, int index) {
        if (!flag) {
            mContainer = (LinearLayout) getChildAt(0);
            flag = true;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mScreenWidth, mScreenHeight);
        if (index == -1)
            mContainer.addView(page, params);
        else
            mContainer.addView(page, index, params);
        mPageCount++;
    }

    /**
     * 移除一个页面
     */
    public void removePage(int index) {
        if (mPageCount < 1) return;
        if (index < 0 || index > mPageCount - 1) return;
        mContainer.removeViewAt(index);
        mPageCount--;
    }

    /**
     * 移除所有的页面
     */
    public void removeAllPages() {
        if (mPageCount > 0) mContainer.removeAllViews();
    }

    /**
     * 获取页面数量
     */
    public int getPageCount() {
        return mPageCount;
    }

    /**
     * 获取相对滑动位置,由右向左滑动,返回正值;由左向右滑动,返回负值
     */
    private int getBaseScrollX() {
        return getScrollX() - mBaseScrollX;
    }

    /**
     * 使相对于基线移动x距离
     */
    private void baseSmoothScrollTo(int x) {
        smoothScrollTo(x + mBaseScrollX, 0);
        int tx = x + mBaseScrollX;
        int mCurrentPage = tx / mScreenWidth;
        if (pageListener != null) pageListener.page(mCurrentPage);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollX = getBaseScrollX();
                int mScrollX = 200;
                if (scrollX > mScrollX) {                          //左滑,大于一半,移到下一页
                    baseSmoothScrollTo(mScreenWidth);
                    mBaseScrollX += mScreenWidth;
                } else if (scrollX > 0) {                          //左滑,不到一半,返回原位
                    baseSmoothScrollTo(0);
                } else if (scrollX > -mScrollX) {                  //右滑,不到一半,返回原位
                    baseSmoothScrollTo(0);
                } else {                                           //右滑,大于一半,移到下一页
                    baseSmoothScrollTo(-mScreenWidth);
                    mBaseScrollX -= mScreenWidth;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    public void setPageListener(PageListener pageListener) {
        this.pageListener = pageListener;
    }

    interface PageListener {
        void page(int page);
    }

}
