package com.tpshop.mall.activity.person.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPOrderBaseActivity;
import com.tpshop.mall.adapter.NavigatorAdaptor;
import com.tpshop.mall.adapter.SPOrderTabAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.model.SPCommentNum;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.SimpleViewPagerDelegate;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_order_center)
public class SPCommentCenterActivity extends SPOrderBaseActivity {

    @ViewById(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;

    @ViewById(R.id.order_view_pager)
    ViewPager mViewPager;

    private SPCommentNum mCommentNum;
    private NavigatorAdaptor naDaptor;
    public static String[] orderTitles;
    private BroadcastReceiver mReceiver;
    private SPOrderTabAdapter fragPagerAdapter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.setCustomerTitle(true, true, getString(R.string.order_center_title));
        super.onCreate(bundle);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SPMobileConstants.ACTION_COMMENT_CHANGE);
        mReceiver = new CommentChangeReceiver();
        registerReceiver(mReceiver, filter);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        refreshData();
        orderTitles = getResources().getStringArray(R.array.top_up_status_order);
        fragPagerAdapter = new SPOrderTabAdapter(getSupportFragmentManager(), orderTitles);
        mViewPager.setAdapter(fragPagerAdapter);
        mViewPager.setOffscreenPageLimit(orderTitles.length);
        initTiles();
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
    }

    private void refreshData() {
        SPUserRequest.getCommentNum(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    mCommentNum = (SPCommentNum) response;
                    naDaptor.updateData(mCommentNum);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
            }
        });
    }

    private void initTiles() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setScrollPivotX(0.15f);
        naDaptor = new NavigatorAdaptor(this, orderTitles, mViewPager);
        commonNavigator.setAdapter(naDaptor);
        mMagicIndicator.setNavigator(commonNavigator);
        mMagicIndicator.setDelegate(new SimpleViewPagerDelegate(mViewPager));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int indext = 0; indext < fragmentManager.getFragments().size(); indext++) {
            Fragment fragment = fragmentManager.getFragments().get(indext);       //找到第一层Fragment
            if (fragment != null)
                handleResult(fragment, requestCode, resultCode, data);
        }
    }

    /**
     * 递归调用,对所有的子Fragment生效
     */
    private void handleResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);      //调用每个Fragment的onActivityResult
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments();      //找到第二层Fragment
        if (childFragment != null)
            for (Fragment f : childFragment)
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    class CommentChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SPMobileConstants.ACTION_COMMENT_CHANGE)) {
                refreshData();
                fragPagerAdapter.updateData();
            }
        }
    }

}
