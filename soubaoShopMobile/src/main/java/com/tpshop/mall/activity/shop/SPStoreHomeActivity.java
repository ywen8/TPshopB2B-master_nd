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
 * Description: 店铺首页
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.DividerGridItemDecoration;
import com.tpshop.mall.adapter.SPStoreHomeAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPStoreRequest;
import com.tpshop.mall.model.SPHomeCategory;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.widget.SPTopPopupMenu;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
@EActivity(R.layout.store_home)
public class SPStoreHomeActivity extends SPBaseActivity implements View.OnClickListener, SPStoreHomeAdapter.OnItemClickListener {

    @ViewById(R.id.store_rlv)
    RecyclerView mRecyclerView;

    private int mStoreId;
    private SPStore mStore;
    SPTopPopupMenu popupMenu;
    SPStoreHomeAdapter mAdapter;
    ArrayList<String> menuItems;
    GridLayoutManager mLayoutManager;
    private StoreChangeReceiver mReceiver;

    @Override
    protected void onCreate(Bundle bundle) {
        setCustomerTitle(true, true, getString(R.string.title_store_home));
        super.onCreate(bundle);
        IntentFilter filter = new IntentFilter(SPMobileConstants.ACTION_STORE_CHANGE);
        filter.addAction(SPMobileConstants.ACTION_ORDER_CHANGE);
        mReceiver = new StoreChangeReceiver();
        registerReceiver(mReceiver, filter);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SPStoreHomeAdapter(this, this);
        mLayoutManager = new GridLayoutManager(this, 2);         //设置排版方式
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(mAdapter, mLayoutManager);
        mLayoutManager.setSpanSizeLookup(lookup);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));        //设置分割线
        mRecyclerView.setAdapter(mAdapter);
        menuItems = new ArrayList<>();        //快捷菜单
        menuItems.add("首页");
        menuItems.add("购物车");
        menuItems.add("用户中心");
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        showLoadingSmallToast();
        mStoreId = getIntent().getIntExtra("storeId", 0);
        SPStoreRequest.getStoreHome(mStoreId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                if (response != null) {
                    mStore = (SPStore) response;
                    refreshView();
                }
            }
        }, new SPFailuredListener(SPStoreHomeActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    public void refreshView() {
        List<SPHomeCategory> homeCategorys = new ArrayList<>();
        if (mStore.getRecommendProducts() != null && mStore.getRecommendProducts().size() > 0) {
            SPHomeCategory recommaendCategory = new SPHomeCategory();
            recommaendCategory.setName("店长推荐");
            recommaendCategory.setGoodsList(mStore.getRecommendProducts());
            homeCategorys.add(recommaendCategory);
        }
        if (mStore.getNewProducts() != null && mStore.getNewProducts().size() > 0) {
            SPHomeCategory newCategory = new SPHomeCategory();
            newCategory.setName("新品上市");
            newCategory.setGoodsList(mStore.getNewProducts());
            homeCategorys.add(newCategory);
        }
        if (mStore.getHotProducts() != null && mStore.getHotProducts().size() > 0) {
            SPHomeCategory hotCategory = new SPHomeCategory();
            hotCategory.setName("热卖单品");
            hotCategory.setGoodsList(mStore.getHotProducts());
            homeCategorys.add(hotCategory);
        }
        mAdapter.updateData(mStore, homeCategorys);
    }

    @Click({R.id.store_category_txtv, R.id.store_about_txtv, R.id.connect_txtv})
    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.store_category_txtv:      //店铺分类
                Intent classIntent = new Intent(this, SPStoreGoodsClassActivity_.class);
                classIntent.putExtra("storeId", mStoreId);
                startActivity(classIntent);
                break;
            case R.id.store_about_txtv:         //店铺简介
                gotoStoreAbout();
                break;
            case R.id.connect_txtv:             //联系客服
                if (!SPStringUtils.isEmpty(mStore.getStoreQQ()))
                    connectCustomer(mStore.getStoreQQ());
                else
                    showToast(getString(R.string.no_contact));
                break;
        }
    }

    /**
     * 店铺简介
     */
    public void gotoStoreAbout() {
        Intent intent = new Intent(this, SPStoreAboutActivity_.class);
        intent.putExtra("storeId", mStore.getStoreId());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_back_btn:
                finish();
                break;
        }
    }

    /**
     * 设置弹窗
     */
    private void setPopup(int type) {
        DisplayMetrics displayMetrics = SPMobileApplication.getInstance().getDisplayMetrics();
        popupMenu = new SPTopPopupMenu(this, displayMetrics.widthPixels, new MenuItemClickListener(), menuItems, type);
    }

    @Override
    public void onItemClick(SPProduct product) {
        Intent intent = new Intent(this, SPProductDetailActivity_.class);
        intent.putExtra("goodsID", product.getGoodsID());
        startActivity(intent);
    }

    @Override
    public void onItemAdClick(int position) {
        List<SPProduct> products = mAdapter.getHomeProduct();
        if (products == null) return;
        if (products.size() > position) {
            SPProduct product = products.get(position);
            Intent intent = new Intent(this, SPProductDetailActivity_.class);
            intent.putExtra("goodsID", product.getGoodsID());
            startActivity(intent);
        }
    }

    @Override
    public void onStoreGoodsAllClick() {
        Intent listIntent = new Intent(SPStoreHomeActivity.this, SPStoreProductListActivity.class);
        listIntent.putExtra("storeId", mStoreId);
        startActivity(listIntent);
    }

    private class MenuItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent mainIntent = new Intent(SPStoreHomeActivity.this, SPMainActivity.class);
            switch (position) {
                case 0:
                    mainIntent.putExtra(SPMainActivity.SELECT_INDEX, SPMainActivity.INDEX_HOME);          //首页
                    break;
                case 1:
                    mainIntent.putExtra(SPMainActivity.SELECT_INDEX, SPMainActivity.INDEX_SHOPCART);      //购物车
                    break;
                case 2:
                    mainIntent.putExtra(SPMainActivity.SELECT_INDEX, SPMainActivity.INDEX_PERSON);        //用户中心
                    break;
            }
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        }
    }

    @Override
    public void showPopuMenu(View v) {
        setPopup(2);
        popupMenu.show(v);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupMenu != null) popupMenu.dismiss();
        unregisterReceiver(mReceiver);
    }

    class StoreChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SPMobileConstants.ACTION_STORE_CHANGE))
                initData();
        }
    }

}
