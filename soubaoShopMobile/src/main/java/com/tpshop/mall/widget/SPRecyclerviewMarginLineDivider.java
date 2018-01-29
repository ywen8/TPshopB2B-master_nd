package com.tpshop.mall.widget;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tpshop.mall.R;

/**
 * Created by admin on 2016/8/11
 */
public class SPRecyclerviewMarginLineDivider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public SPRecyclerviewMarginLineDivider(Resources resources) {
        mDivider = resources.getDrawable(R.drawable.recyclerview_margin_line_divider);
    }

    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

}
