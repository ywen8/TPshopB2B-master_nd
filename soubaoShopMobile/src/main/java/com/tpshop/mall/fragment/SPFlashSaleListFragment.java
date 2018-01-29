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
 * Description:SPFlashSaleListFragment
 *
 * @version V1.0
 */
package com.tpshop.mall.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.headerfooter.songhang.library.SmartRecyclerAdapter;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.shop.SPProductDetailActivity_;
import com.tpshop.mall.adapter.ListDividerItemDecoration;
import com.tpshop.mall.adapter.SPFlashSaleListAdapter;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.shop.SPFlashSale;
import com.tpshop.mall.model.shop.SPFlashTime;
import com.tpshop.mall.utils.SPUtils;
import com.tpshop.mall.widget.swipetoloadlayout.OnLoadMoreListener;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 抢购/秒杀/列表fragment
 */
public class SPFlashSaleListFragment extends SPBaseFragment implements OnRefreshListener, OnLoadMoreListener,
        SPFlashSaleListAdapter.OnItemClickListener {

    int mPageIndex;
    View mHeaderView;
    SPFlashTime mFlashTime;
    public Context mContext;
    TextView remainTitleTxtv;
    TextView mRemainHintTxtv;                      //提示
    TextView mRemainTimeTxtv;                      //时间
    SPFlashSaleListAdapter mAdapter;
    private EndTimeRunnable runnable;
    private StartTimeRunnable runnable2;
    SmartRecyclerAdapter mSmartRecyclerAdapter;
    SuperRefreshRecyclerView refreshRecyclerView;
    List<SPFlashSale> flashSales = new ArrayList<>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    public static SPFlashSaleListFragment newInstants(SPFlashTime flashTime) {
        SPFlashSaleListFragment fragment = new SPFlashSaleListFragment();
        fragment.mFlashTime = flashTime;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flash_sale_list_fragment_view, null, false);
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
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.flash_sale_header, null);
        remainTitleTxtv = (TextView) mHeaderView.findViewById(R.id.remain_title_txtv);
        mRemainTimeTxtv = (TextView) mHeaderView.findViewById(R.id.remain_time_txtv);
        mRemainHintTxtv = (TextView) mHeaderView.findViewById(R.id.remain_hint_txtv);
        refreshRecyclerView.init(new LinearLayoutManager(getActivity()), this, this);
        Drawable drawable = getResources().getDrawable(R.drawable.divider_grid_product_list);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(drawable));             //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        mAdapter = new SPFlashSaleListAdapter(getActivity(), mFlashTime, this);
        mSmartRecyclerAdapter = new SmartRecyclerAdapter(mAdapter);
        mSmartRecyclerAdapter.setHeaderView(mHeaderView);
        refreshRecyclerView.setAdapter(mSmartRecyclerAdapter);
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
        SPShopRequest.getFlashSaleList(mPageIndex, mFlashTime, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    flashSales = (List<SPFlashSale>) response;
                    mAdapter.setData(flashSales);
                }
                refreshTitle();
            }
        }, new SPFailuredListener() {
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
        SPShopRequest.getFlashSaleList(mPageIndex, mFlashTime, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    List<SPFlashSale> sales = (List<SPFlashSale>) response;
                    flashSales.addAll(sales);
                    mAdapter.setData(flashSales);
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
    public void onItemClick(String goodsId, String itemId) {
        Intent intent = new Intent(getActivity(), SPProductDetailActivity_.class);
        intent.putExtra("goodsID", goodsId);
        if (!SPStringUtils.isEmpty(itemId)) intent.putExtra("itemID", itemId);
        getActivity().startActivity(intent);
    }

    public void refreshTitle() {
        if (mFlashTime.getType() == 1) {        //正在开抢
            if (mAdapter.getItemCount() < 1) {
                mRemainHintTxtv.setTextColor(Color.parseColor("#666666"));
                mRemainHintTxtv.setText("无抢购商品");
                remainTitleTxtv.setVisibility(View.GONE);
                mRemainTimeTxtv.setVisibility(View.GONE);
            } else {
                mRemainTimeTxtv.setTextColor(Color.parseColor("#e23435"));
                mRemainHintTxtv.setVisibility(View.VISIBLE);
                remainTitleTxtv.setText("正在秒杀，先下单先得哦~");
                remainTitleTxtv.setVisibility(View.VISIBLE);
                setCountTime();
            }
        } else if (mFlashTime.getType() == 2) {       //即将开枪
            mRemainTimeTxtv.setTextColor(Color.parseColor("#666666"));
            remainTitleTxtv.setText("秒杀活动即将开场~");
            remainTitleTxtv.setVisibility(View.VISIBLE);
            mRemainHintTxtv.setText("距离开始");
            mRemainHintTxtv.setVisibility(View.VISIBLE);
            setStartTime();
        }
    }

    //开始倒计时
    private void setStartTime() {
        Long[] times = SPUtils.getTimeCut(System.currentTimeMillis() / 1000, Long.valueOf(mFlashTime.getStartTime()));
        String groupTip = times[1] + "时" + times[2] + "分" + times[3] + "秒";
        mRemainTimeTxtv.setText(groupTip);
        if (runnable2 != null) runnable2.stop();
        runnable2 = new StartTimeRunnable();
        new Handler().postDelayed(runnable2, 1000);
    }

    //抢购倒计时
    private void setCountTime() {
        Long[] times = SPUtils.getTimeCut(System.currentTimeMillis() / 1000, Long.valueOf(mFlashTime.getEndTime()));
        if (times.length == 4 && times[0] == 0 && times[1] == 0 && times[2] == 0 && times[3] == 0) {
            mRemainHintTxtv.setText("已经结束");
            mRemainTimeTxtv.setVisibility(View.GONE);
        } else {
            String groupTip = times[1] + "时" + times[2] + "分" + times[3] + "秒";
            mRemainTimeTxtv.setText(groupTip);
            mRemainHintTxtv.setText("距离结束");
            mRemainHintTxtv.setVisibility(View.VISIBLE);
        }
        if (runnable != null) runnable.stop();
        runnable = new EndTimeRunnable();
        new Handler().postDelayed(runnable, 1000);
    }

    private class EndTimeRunnable implements Runnable {
        private boolean isStop = false;

        private void stop() {
            isStop = true;
        }

        @Override
        public void run() {
            if (!isStop)
                setCountTime();
        }
    }

    private class StartTimeRunnable implements Runnable {
        private boolean isStop = false;

        private void stop() {
            isStop = true;
        }

        @Override
        public void run() {
            if (!isStop)
                setStartTime();
        }
    }

}
