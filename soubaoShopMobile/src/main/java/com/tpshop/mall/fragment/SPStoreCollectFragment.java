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
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.person.SPCollectListActivity;
import com.tpshop.mall.activity.shop.SPStoreHomeActivity_;
import com.tpshop.mall.adapter.ListDividerItemDecoration;
import com.tpshop.mall.adapter.SPStoreCollectListAdapter;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.http.shop.SPStoreRequest;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPConfirmDialog;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户中心 -> 店铺收藏
 */
public class SPStoreCollectFragment extends SPBaseFragment implements OnRefreshListener, SPStoreCollectListAdapter.StoreCollectListener,
        SPConfirmDialog.ConfirmDialogListener {

    List<SPStore> mStores;
    private SPStore mStore;
    public Context mContext;
    SPStoreCollectListAdapter mAdapter;
    SuperRefreshRecyclerView refreshRecyclerView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_goods_collect_list, null, false);
        super.init(view);
        return view;
    }

    @Override
    public void initSubView(View view) {
        refreshRecyclerView = (SuperRefreshRecyclerView) view.findViewById(R.id.super_recyclerview);
        View emptyView = view.findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        refreshRecyclerView.init(new LinearLayoutManager(getActivity()), this, null);
        Drawable drawable = getResources().getDrawable(R.drawable.divider_grid_product_list);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(drawable));             //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(false);
        mAdapter = new SPStoreCollectListAdapter(getActivity(), this);
        refreshRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
        refreshData();
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    public void refreshData() {
        showLoadingSmallToast();
        SPPersonRequest.getCollectStore(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    mStores = (List<SPStore>) response;
                    mAdapter.setData(mStores);
                    refreshRecyclerView.showData();
                } else {
                    refreshRecyclerView.showEmpty();
                }
            }
        }, new SPFailuredListener((SPCollectListActivity) getActivity()) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                showFailedToast(msg);
            }
        });
    }

    @Override
    public void onCancelStore(SPStore store) {
        this.mStore = store;
        showConfirmDialog("确定删除收藏吗？", "删除提醒", this, 1);
    }

    @Override
    public void clickOk(int actionType) {
        showLoadingSmallToast();
        SPStoreRequest.collectOrCancelStoreWithID(mStore.getStoreId(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showSuccessToast(msg);
                refreshData();
            }
        }, new SPFailuredListener((SPCollectListActivity) getActivity()) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    @Override
    public void onDetailStore(SPStore store) {
        Intent intent = new Intent(getActivity(), SPStoreHomeActivity_.class);
        intent.putExtra("storeId", store.getStoreId());
        startActivity(intent);
    }

}
