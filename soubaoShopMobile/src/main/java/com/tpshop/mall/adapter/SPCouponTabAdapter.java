package com.tpshop.mall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tpshop.mall.fragment.SPCouponListFragment;

public class SPCouponTabAdapter extends FragmentPagerAdapter {

    private static String[] couponTitles;                //Titles的标识
    private SPCouponListFragment usedFragment;           //已用
    private SPCouponListFragment unuseFragment;          //未用
    private SPCouponListFragment expireFragment;         //过期

    public SPCouponTabAdapter(FragmentManager fm, String[] couponTitles) {
        super(fm);
        SPCouponTabAdapter.couponTitles = couponTitles;
        unuseFragment = SPCouponListFragment.newInstance(0);
        usedFragment = SPCouponListFragment.newInstance(1);
        expireFragment = SPCouponListFragment.newInstance(2);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return unuseFragment;
        } else if (position == 1) {
            return usedFragment;
        } else {
            return expireFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return couponTitles[position];
    }

    @Override
    public int getCount() {
        return couponTitles.length;
    }

}
