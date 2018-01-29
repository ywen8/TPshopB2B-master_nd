package com.tpshop.mall.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tpshop.mall.R;
import com.tpshop.mall.model.SPCommentNum;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

public class NavigatorAdaptor extends CommonNavigatorAdapter {

    private String mNoNum;
    private String mHadNum;
    private String mServeNum;
    private Context mContext;
    private ViewPager mViewPager;
    private String[] mOrderTitles;

    public NavigatorAdaptor(Context context, String[] orderTitles, ViewPager viewPager) {
        this.mContext = context;
        this.mOrderTitles = orderTitles;
        this.mViewPager = viewPager;
    }

    @Override
    public int getCount() {
        return (mOrderTitles == null) ? 0 : mOrderTitles.length;
    }

    public void updateData(SPCommentNum commentNum) {
        if (commentNum == null) return;
        this.mNoNum = commentNum.getNoCommentNum();
        this.mHadNum = commentNum.getHadCommentNum();
        this.mServeNum = commentNum.getServeCommentNum();
        this.notifyDataSetChanged();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
        simplePagerTitleView.setSingleLine(false);
        if (index == 0) {
            if (mNoNum == null || mNoNum.equals("") || mNoNum.equals("0"))
                simplePagerTitleView.setText(mOrderTitles[index]);
            else
                simplePagerTitleView.setText(mOrderTitles[index] + "\n" + mNoNum);
            if (mNoNum == null && mHadNum == null && mServeNum == null)
                simplePagerTitleView.setText(mOrderTitles[index]);
        } else if (index == 1) {
            if (mHadNum == null || mHadNum.equals("") || mHadNum.equals("0"))
                simplePagerTitleView.setText(mOrderTitles[index]);
            else
                simplePagerTitleView.setText(mOrderTitles[index] + "\n" + mHadNum);
            if (mNoNum == null && mHadNum == null && mServeNum == null)
                simplePagerTitleView.setText(mOrderTitles[index]);
        } else {
            if (mServeNum == null || mServeNum.equals("") || mServeNum.equals("0"))
                simplePagerTitleView.setText(mOrderTitles[index]);
            else
                simplePagerTitleView.setText(mOrderTitles[index] + "\n" + mServeNum);
            if (mNoNum == null && mHadNum == null && mServeNum == null)
                simplePagerTitleView.setText(mOrderTitles[index]);
        }
        simplePagerTitleView.setNormalColor(mContext.getResources().getColor(R.color.sub_title));
        simplePagerTitleView.setSelectedColor(mContext.getResources().getColor(R.color.light_red));
        simplePagerTitleView.setTextSize(12);
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(index);
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
        indicator.setYOffset(UIUtil.dip2px(context, 1));
        indicator.setColors(mContext.getResources().getColor(R.color.light_red));
        return indicator;
    }

}
