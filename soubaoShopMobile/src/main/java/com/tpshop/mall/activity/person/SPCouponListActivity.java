/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年11月12日 下午8:08:13
 * Description:商品详情 -> 商品评论, 图文详情
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.activity.shop.SPStoreHomeActivity_;
import com.tpshop.mall.adapter.SPCouponListAdapter;
import com.tpshop.mall.adapter.SPCouponTabAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.shop.SPCoupon;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.SimpleViewPagerDelegate;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.person_coupon_main_view)
public class SPCouponListActivity extends SPBaseActivity implements SPCouponListAdapter.ItemClickListener {

    private int storeId = 0;
    private double orderMoney = 0;
    public static String[] couponTitles;
    FragmentPagerAdapter fragPagerAdapter;

    @ViewById(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;

    @ViewById(R.id.coupon_view_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_coupon));
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            storeId = getIntent().getIntExtra(SPMobileConstants.KEY_STORE_ID, 0);
            orderMoney = getIntent().getDoubleExtra("order_money", 0);
        }
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        couponTitles = getResources().getStringArray(R.array.coupon_status_name);
        fragPagerAdapter = new SPCouponTabAdapter(getSupportFragmentManager(), couponTitles);
        mViewPager.setAdapter(fragPagerAdapter);
        mViewPager.setOffscreenPageLimit(couponTitles.length);
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setScrollPivotX(0.15f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return (couponTitles == null) ? 0 : couponTitles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(couponTitles[index]);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.sub_title));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.light_red));
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
                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setColors(getResources().getColor(R.color.light_red));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        mMagicIndicator.setDelegate(new SimpleViewPagerDelegate(mViewPager));
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
    }

    public int getStoreId() {
        return this.storeId;
    }

    public double getOrderMoney() {
        return this.orderMoney;
    }

    @Override
    public void onItemClick(int type, SPCoupon coupon) {
        if (type == 0) {     //进入店铺使用
            Intent intent = new Intent(this, SPStoreHomeActivity_.class);
            intent.putExtra("storeId", coupon.getStoreId());
            startActivity(intent);
        }
    }

}
