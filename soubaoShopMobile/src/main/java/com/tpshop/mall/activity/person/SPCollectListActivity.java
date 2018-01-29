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
 * Date: @date 2015年11月3日 下午10:04:49
 * Description: 商品收藏列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.person;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.SPCollectTabAdapter;

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

import java.util.Arrays;
import java.util.List;

/**
 * @author 飞龙
 */
@EActivity(R.layout.person_collect_main_view)
public class SPCollectListActivity extends SPBaseActivity {

    FragmentPagerAdapter fragPagerAdapter;
    String[] channels = new String[]{"商品收藏", "店铺关注"};
    List<String> mDataList = Arrays.asList(channels);

    @ViewById(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;

    @ViewById(R.id.collect_view_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle bundle) {
        super.setCustomerTitle(true, true, getString(R.string.title_collect));
        super.onCreate(bundle);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        fragPagerAdapter = new SPCollectTabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(fragPagerAdapter);
        int page = getIntent().getIntExtra("index", 0);
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setScrollPivotX(0.15f);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {       //指示器显示到指定页面
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                commonNavigator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                commonNavigator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                commonNavigator.onPageScrollStateChanged(state);
            }
        });
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
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
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setColors(getResources().getColor(R.color.light_red));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        mMagicIndicator.setDelegate(new SimpleViewPagerDelegate(mViewPager));
        mViewPager.setCurrentItem(page);
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
    }

}
