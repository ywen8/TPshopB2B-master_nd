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
package com.tpshop.mall.activity.person.order;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.ListDividerItemDecoration;
import com.tpshop.mall.adapter.SPOrderCouponListAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.model.shop.SPCoupon;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.order_coupon_list)
public class SPOrderCouponListActivity extends SPBaseActivity implements OnRefreshListener, SPOrderCouponListAdapter.ItemClickListener {

    private int storeId = 0;
    private TextView tvCommit;
    private double orderMoney = 0;
    String itemId, goodsId, goodsNum, action;
    private SPOrderCouponListAdapter mAdapter;
    private List<SPCoupon> coupons = new ArrayList<>();
    private SuperRefreshRecyclerView refreshRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "使用优惠券");
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            storeId = getIntent().getIntExtra(SPMobileConstants.KEY_STORE_ID, 0);
            orderMoney = getIntent().getDoubleExtra("order_money", 0);
            itemId = getIntent().getStringExtra("item_id");
            goodsId = getIntent().getStringExtra("goods_id");
            goodsNum = getIntent().getStringExtra("goods_num");
            action = getIntent().getStringExtra("action");
        }
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        refreshRecyclerView = (SuperRefreshRecyclerView) findViewById(R.id.super_recyclerview);
        View mEmptyView = findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(mEmptyView);
        refreshRecyclerView = (SuperRefreshRecyclerView) findViewById(R.id.super_recyclerview);
        refreshRecyclerView.init(new LinearLayoutManager(this), this, null);
        Drawable drawable = getResources().getDrawable(R.drawable.divider_white_large);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(drawable));        //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(false);
        FrameLayout titlebarLayout = (FrameLayout) findViewById(R.id.titlebar_normal_layout);
        tvCommit = new TextView(this);
        tvCommit.setText("确定");
        tvCommit.setTextColor(this.getResources().getColor(R.color.light_red));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        tvCommit.setPadding(0, 0, 10, 0);
        params.setMargins(0, 0, 10, 0);
        tvCommit.setGravity(Gravity.CENTER_VERTICAL);
        tvCommit.setTextSize(16);
        titlebarLayout.addView(tvCommit, params);
        mAdapter = new SPOrderCouponListAdapter(this, this);
        refreshRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCoupon();
            }
        });
    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    public void refreshData() {
        showLoadingSmallToast();
        RequestParams params = new RequestParams();
        params.put("store_id", storeId);
        params.put("money", orderMoney);
        if (itemId != null)
            params.put("item_id", itemId);
        if (goodsId != null)
            params.put("goods_id", goodsId);
        if (goodsNum != null)
            params.put("goods_num", goodsNum);
        if (action != null)
            params.put("action", action);
        SPPersonRequest.getOrderCouponList(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    coupons = (List<SPCoupon>) response;
                    dealModel();
                    mAdapter.updateData(coupons);
                    refreshRecyclerView.showData();
                } else {
                    refreshRecyclerView.showEmpty();
                }
            }
        }, new SPFailuredListener(SPOrderCouponListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                showFailedToast(msg);
            }
        });
    }

    public void dealModel() {
        for (SPCoupon coupon : coupons)
            coupon.setIsCheck(false);
    }

    @Override
    public void onItemClick(SPCoupon coupon, boolean checked) {
        for (SPCoupon spCoupon : coupons)      //将所有优惠券设为不选,实现单选
            spCoupon.setIsCheck(false);
        for (SPCoupon item : coupons)
            if (item == coupon)
                item.setIsCheck(checked);
        mAdapter.updateData(coupons);
    }

    private void selectCoupon() {
        boolean hasCoupon = false;
        for (SPCoupon coupon : coupons) {
            if (coupon.isCheck()) {      //使用优惠券
                hasCoupon = true;
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectCoupon", coupon);
                resultIntent.putExtra("storeId", storeId);
                setResult(SPMobileConstants.Result_Code_GetCoupon, resultIntent);
            }
        }
        if (!hasCoupon) {      //不使用优惠券
            Intent resultIntent = new Intent();
            resultIntent.putExtra("storeId", storeId);
            setResult(SPMobileConstants.Result_Code_GetCoupon, resultIntent);
        }
        finish();
    }

}
