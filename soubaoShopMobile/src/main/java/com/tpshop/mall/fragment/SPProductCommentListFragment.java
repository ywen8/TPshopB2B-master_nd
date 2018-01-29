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
 * Description: 商品评论
 *
 * @version V1.0
 */
package com.tpshop.mall.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPImagePreviewActivity_;
import com.tpshop.mall.activity.shop.SPProductDetailActivity;
import com.tpshop.mall.adapter.DividerGridItemDecoration;
import com.tpshop.mall.adapter.SPProductDetailCommentAdapter;
import com.tpshop.mall.entity.SPCommentTitleModel;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.shop.SPGoodsComment;
import com.tpshop.mall.widget.swipetoloadlayout.OnLoadMoreListener;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情 -> 商品评论列表
 */
public class SPProductCommentListFragment extends SPBaseFragment implements SPProductDetailCommentAdapter.OnImageClickListener, OnRefreshListener,
        OnLoadMoreListener, View.OnClickListener {

    int pageIndex;
    int mType = 0;               //评论类型,1全部,2好评,3中评,4差评,5晒图,默认1
    Button allBtn;
    Button badBtn;
    Button greatBtn;
    Button haspicBtn;
    Button averageBtn;
    boolean isFirstLoad;
    private String goodsId;
    List<SPGoodsComment> mComments;
    SPProductDetailCommentAdapter mAdapter;
    private SPProductDetailActivity mActivity;
    SuperRefreshRecyclerView refreshRecyclerView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (SPProductDetailActivity) activity;
        if (this.mActivity != null)
            goodsId = mActivity.getProductId();
    }

    public static SPProductCommentListFragment newInstence(int type) {
        SPProductCommentListFragment fragment = new SPProductCommentListFragment();
        fragment.mType = type;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.product_details_comment_list, null, false);
        super.init(view);
        return view;
    }

    @Override
    public void initSubView(View view) {
        View emptyView = view.findViewById(R.id.empty_rlayout);
        refreshRecyclerView = (SuperRefreshRecyclerView) view.findViewById(R.id.super_recyclerview);
        refreshRecyclerView.setEmptyView(emptyView);
        refreshRecyclerView.init(new LinearLayoutManager(getActivity()), this, this);
        refreshRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));        //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        mAdapter = new SPProductDetailCommentAdapter(mActivity);
        refreshRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnImageListener(this);
        greatBtn = (Button) view.findViewById(R.id.great_btn);
        allBtn = (Button) view.findViewById(R.id.all_btn);
        badBtn = (Button) view.findViewById(R.id.bad_btn);
        averageBtn = (Button) view.findViewById(R.id.average_btn);
        haspicBtn = (Button) view.findViewById(R.id.haspic_btn);
    }

    @Override
    public void initEvent() {
        allBtn.setOnClickListener(this);
        greatBtn.setOnClickListener(this);
        averageBtn.setOnClickListener(this);
        badBtn.setOnClickListener(this);
        haspicBtn.setOnClickListener(this);
    }

    @Override
    public void initData() {
        SPCommentTitleModel titleModel = mActivity.getProduct().getCommentTitleModel();
        if (titleModel != null) {
            allBtn.setText("全部(" + titleModel.getAll() + ")");
            greatBtn.setText("好评(" + titleModel.getGreat() + ")");
            averageBtn.setText("中评(" + titleModel.getAverage() + ")");
            badBtn.setText("差评(" + titleModel.getBad() + ")");
            haspicBtn.setText("有图(" + titleModel.getHasPic() + ")");
        } else {
            allBtn.setText("全部(0)");
            greatBtn.setText("好评(0)");
            averageBtn.setText("中评(0)");
            badBtn.setText("差评(0)");
            haspicBtn.setText("有图(0)");
        }
    }

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
        refreshData();
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
        refreshButtonStyle();
        pageIndex = 1;
        showLoadingSmallToast();
        SPShopRequest.getGoodsCommentWithGoodsID(goodsId, pageIndex, mType, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    mComments = (List<SPGoodsComment>) response;
                    mAdapter.setData(mComments);
                    refreshRecyclerView.showData();
                } else {
                    refreshRecyclerView.showEmpty();
                }
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
        pageIndex++;
        SPShopRequest.getGoodsCommentWithGoodsID(goodsId, pageIndex, mType, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null) {
                    List<SPGoodsComment> tempComment = (List<SPGoodsComment>) response;
                    mComments.addAll(tempComment);
                    mAdapter.setData(mComments);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
                pageIndex--;
            }
        });
    }

    @Override
    public void onClickListener(List<String> imgUrls, int position) {
        SPMobileApplication.getInstance().setImageUrl(imgUrls);
        Intent previewIntent = new Intent(getActivity(), SPImagePreviewActivity_.class);
        previewIntent.putExtra("index", position);
        startActivity(previewIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_btn:
                mType = 1;
                break;
            case R.id.great_btn:
                mType = 2;
                break;
            case R.id.average_btn:
                mType = 3;
                break;
            case R.id.bad_btn:
                mType = 4;
                break;
            case R.id.haspic_btn:
                mType = 5;
                break;
        }
        refreshData();
    }

    /**
     * 刷新按钮状态
     */
    private void refreshButtonStyle() {
        allBtn.setTextColor(Color.parseColor("#6B6F78"));
        allBtn.setBackgroundResource(R.drawable.detail_comment_title_pink_corner_button);
        greatBtn.setTextColor(Color.parseColor("#6B6F78"));
        greatBtn.setBackgroundResource(R.drawable.detail_comment_title_pink_corner_button);
        averageBtn.setTextColor(Color.parseColor("#6B6F78"));
        averageBtn.setBackgroundResource(R.drawable.detail_comment_title_pink_corner_button);
        badBtn.setTextColor(Color.parseColor("#6B6F78"));
        badBtn.setBackgroundResource(R.drawable.detail_comment_title_pink_corner_button);
        haspicBtn.setTextColor(Color.parseColor("#6B6F78"));
        haspicBtn.setBackgroundResource(R.drawable.detail_comment_title_pink_corner_button);
        switch (mType) {
            case 1:
                allBtn.setTextColor(Color.parseColor("#FFFFFF"));
                allBtn.setBackgroundResource(R.drawable.detail_comment_title_red_corner_button);
                break;
            case 2:
                greatBtn.setTextColor(Color.parseColor("#FFFFFF"));
                greatBtn.setBackgroundResource(R.drawable.detail_comment_title_red_corner_button);
                break;
            case 3:
                averageBtn.setTextColor(Color.parseColor("#FFFFFF"));
                averageBtn.setBackgroundResource(R.drawable.detail_comment_title_red_corner_button);
                break;
            case 4:
                badBtn.setTextColor(Color.parseColor("#FFFFFF"));
                badBtn.setBackgroundResource(R.drawable.detail_comment_title_red_corner_button);
                break;
            case 5:
                haspicBtn.setTextColor(Color.parseColor("#FFFFFF"));
                haspicBtn.setBackgroundResource(R.drawable.detail_comment_title_red_corner_button);
                break;
        }
    }

}
