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
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.person.SPCouponCenterActivity;
import com.tpshop.mall.activity.shop.SPStoreHomeActivity_;
import com.tpshop.mall.adapter.ListDividerItemDecoration;
import com.tpshop.mall.adapter.SPCouponCenterListAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.model.SPCategory;
import com.tpshop.mall.model.shop.SPCoupon;
import com.tpshop.mall.widget.swipetoloadlayout.OnLoadMoreListener;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 领券中心 -> 优惠券
 */
public class SPCouponCenterFragment extends SPBaseFragment implements OnRefreshListener, OnLoadMoreListener,
        SPCouponCenterListAdapter.OnItemClickListener {

    int mPageIndex;
    View mEmptyView;
    SPCategory mCategory;
    SPCouponCenterListAdapter mAdapter;
    private SPCouponCenterActivity activity;
    List<SPCoupon> coupons = new ArrayList<>();
    SuperRefreshRecyclerView refreshRecyclerView;

    public static SPCouponCenterFragment newInstance(SPCategory category) {
        SPCouponCenterFragment fragment = new SPCouponCenterFragment();
        fragment.mCategory = category;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SPCouponCenterActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_coupon_center_fragment_view, null, false);
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
        refreshRecyclerView.init(new LinearLayoutManager(getActivity()), this, this);
        Drawable divilder = getResources().getDrawable(R.drawable.divider_activity_color_large);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(divilder));             //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        mAdapter = new SPCouponCenterListAdapter(activity, this);
        refreshRecyclerView.setAdapter(mAdapter);
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
        SPPersonRequest.getCouponCenterWithCategoryId(mPageIndex, mCategory.getId(), new SPSuccessListener() {
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
        }, new SPFailuredListener(activity) {
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
        SPPersonRequest.getCouponCenterWithCategoryId(mPageIndex, mCategory.getId(), new SPSuccessListener() {
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
    public void onGetItemClick(final SPCoupon coupon) {          //领取优惠券
        showLoadingSmallToast();
        SPPersonRequest.gainCoupon(coupon.getCouponID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showToast(msg);
                notifyAdapterChange(coupon.getCouponID());
                activity.sendBroadcast(new Intent(SPMobileConstants.ACTION_COUPON_CHANGE));
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showToast(msg);
            }
        });
    }

    @Override
    public void onCostItemClick(final SPCoupon coupon) {            //去使用
        Intent intent = new Intent(getActivity(), SPStoreHomeActivity_.class);
        intent.putExtra("storeId", coupon.getStoreId());
        getActivity().startActivity(intent);
    }

    private void notifyAdapterChange(String couponId) {
        if (coupons == null) return;
        for (SPCoupon coupon : coupons) {
            if (coupon.getCouponID().equals(couponId)) {
                coupon.setIsget(1);         //设置已经领取
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

}
