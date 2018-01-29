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
 * Date: @date 2015年10月20日 下午7:19:26
 * Description:MineFragment
 *
 * @version V1.0
 */
package com.tpshop.mall.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.person.SPCouponCenterActivity_;
import com.tpshop.mall.activity.person.SPCouponListActivity;
import com.tpshop.mall.adapter.DividerGridItemDecoration;
import com.tpshop.mall.adapter.SPCouponListAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.model.shop.SPCoupon;
import com.tpshop.mall.widget.swipetoloadlayout.OnLoadMoreListener;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券列表 -> 优惠券
 */
public class SPCouponListFragment extends SPBaseFragment implements OnRefreshListener, OnLoadMoreListener {

    int mPageIndex;
    View mEmptyView;
    protected int mType;
    SPCouponListAdapter mAdapter;
    private SPCouponListActivity activity;
    private CouponChangeReceiver mReceiver;
    List<SPCoupon> coupons = new ArrayList<>();
    SuperRefreshRecyclerView refreshRecyclerView;

    public static SPCouponListFragment newInstance(int type) {
        SPCouponListFragment fragment = new SPCouponListFragment();
        fragment.mType = type;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SPCouponListActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_coupon_fragment_view, null, false);
        IntentFilter filter = new IntentFilter(SPMobileConstants.ACTION_COUPON_CHANGE);
        mReceiver = new CouponChangeReceiver();
        activity.registerReceiver(mReceiver, filter);
        super.init(view);
        return view;
    }

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
        refreshData();
    }

    @Override
    public void initSubView(View view) {
        refreshRecyclerView = (SuperRefreshRecyclerView) view.findViewById(R.id.super_recyclerview);
        mEmptyView = view.findViewById(R.id.empty_rlayout);
        refreshRecyclerView.setEmptyView(mEmptyView);
        refreshRecyclerView.init(new GridLayoutManager(getActivity(), 2), this, this);
        Drawable divilder = getResources().getDrawable(R.drawable.divider_coupon_grid_item);
        refreshRecyclerView.addItemDecoration(new DividerGridItemDecoration(divilder));                //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        mAdapter = new SPCouponListAdapter(activity, mType, activity);
        refreshRecyclerView.setAdapter(mAdapter);
        view.findViewById(R.id.look_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SPCouponCenterActivity_.class);                   //去领取
                startActivity(intent);
            }
        });
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onLoadMore() {
        loadMoreData();
    }

    public void refreshData() {
        mPageIndex = 1;
        showLoadingSmallToast();
        SPPersonRequest.getCouponListWithType(mType, activity.getStoreId(), activity.getOrderMoney(), mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    coupons = (List<SPCoupon>) response;
                    mAdapter.updateData(coupons);
                    refreshRecyclerView.showData();
                } else {
                    refreshRecyclerView.showEmpty();
                }
            }
        }, new SPFailuredListener((SPCouponListActivity) getActivity()) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                showFailedToast(msg);
            }
        });
    }

    public void loadMoreData() {
        mPageIndex++;
        SPPersonRequest.getCouponListWithType(mType, activity.getStoreId(), activity.getOrderMoney(), mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    List<SPCoupon> tempComment = (List<SPCoupon>) response;
                    coupons.addAll(tempComment);
                    mAdapter.updateData(coupons);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
                mPageIndex--;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activity != null) activity.unregisterReceiver(mReceiver);
    }

    class CouponChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SPMobileConstants.ACTION_COUPON_CHANGE))
                refreshData();
        }
    }

}
