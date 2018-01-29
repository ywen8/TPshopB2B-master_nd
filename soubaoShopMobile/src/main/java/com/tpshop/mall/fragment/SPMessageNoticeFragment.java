package com.tpshop.mall.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.person.order.SPOrderDetailActivity_;
import com.tpshop.mall.activity.shop.SPStoreHomeActivity_;
import com.tpshop.mall.adapter.SPMessageAssetAdapter;
import com.tpshop.mall.adapter.SPMessageLogisticsAdapter;
import com.tpshop.mall.adapter.SPMessageNoticeAdapter;
import com.tpshop.mall.adapter.SPMessageServiceAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.model.SPMessageList;
import com.tpshop.mall.model.order.SPOrder;
import com.tpshop.mall.widget.swipetoloadlayout.OnLoadMoreListener;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuhao
 */
public class SPMessageNoticeFragment extends SPBaseFragment implements OnRefreshListener, OnLoadMoreListener {

    int pageIndex;
    View mEmptyView;
    Context mContext;
    private int mType;                      //1.促销活动mType=0,2.物流通知mType=1,3.资金变动mType=2,4.服务通知mType=3
    List<SPMessageList> messageList;
    private SPMessageAssetAdapter mAdapter4;
    private SPMessageNoticeAdapter mAdapter2;
    private SPMessageServiceAdapter mAdapter0;
    private SPMessageLogisticsAdapter mAdapter1;
    public SectionedRecyclerViewAdapter mCurrcentAdapter;
    private SuperRefreshRecyclerView refreshRecyclerView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SPMobileConstants.MSG_CODE_ORDER_LIST_ITEM_ACTION:
                    Intent detailIntent = new Intent(getActivity(), SPOrderDetailActivity_.class);
                    detailIntent.putExtra("orderId", ((SPOrder) msg.obj).getOrderID());
                    startActivity(detailIntent);
                    break;
                case SPMobileConstants.MSG_CODE_STORE_HOME_ACTION:     //店铺首页
                    Intent storeIntent = new Intent(getActivity(), SPStoreHomeActivity_.class);
                    storeIntent.putExtra("storeId", msg.obj.toString());
                    startActivity(storeIntent);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_notice_fragment, null, false);
        super.init(view);
        return view;
    }

    public SPMessageNoticeFragment newInstance(Context context, int type) {
        SPMessageNoticeFragment fragment = new SPMessageNoticeFragment();
        fragment.mType = type;
        fragment.mContext = context;
        return fragment;
    }

    @Override
    public void initSubView(View view) {
        mEmptyView = view.findViewById(R.id.empty_rlayout);
        refreshRecyclerView = (SuperRefreshRecyclerView) view.findViewById(R.id.super_recyclerview);
        TextView tv = (TextView) view.findViewById(R.id.empty_txtv);
        if (mType == 2) {
            tv.setText("您现在没有促销活动哦~");
        } else if (mType == 1) {
            tv.setText("您现在没有物流通知哦~");
        } else if (mType == 4) {
            tv.setText("您现在没有资金变动哦~");
        } else if (mType == 0) {
            tv.setText("您现在没有服务通知哦~");
        }
        refreshRecyclerView.setEmptyView(mEmptyView);
        refreshRecyclerView.init(new LinearLayoutManager(getActivity()), this, this);
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        if (mType == 0) {
            mAdapter0 = new SPMessageServiceAdapter(getActivity(), mHandler);
            mCurrcentAdapter = mAdapter0;
            refreshRecyclerView.setAdapter(mAdapter0);
        } else if (mType == 1) {
            mAdapter1 = new SPMessageLogisticsAdapter(getActivity(), mHandler);
            mCurrcentAdapter = mAdapter1;
            refreshRecyclerView.setAdapter(mAdapter1);
        } else if (mType == 2) {
            mAdapter2 = new SPMessageNoticeAdapter(getActivity(), mHandler);
            mCurrcentAdapter = mAdapter2;
            refreshRecyclerView.setAdapter(mAdapter2);
        } else if (mType == 4) {
            mAdapter4 = new SPMessageAssetAdapter(getActivity(), mHandler);
            mCurrcentAdapter = mAdapter4;
            refreshRecyclerView.setAdapter(mAdapter4);
        }
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
    }

    @Override
    public void initData() {
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
        pageIndex = 1;
        showLoadingSmallToast();
        SPUserRequest.getMessageList(mType, pageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    messageList = (List<SPMessageList>) response;
                    updateData(messageList);
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

    //0系统消息,1物流通知,2优惠促销,3商品提醒,4我的资产,5商城好店
    private void updateData(List<SPMessageList> mList) {
        if (mType == 0) {
            mAdapter0.updateData(mList);
        } else if (mType == 1) {
            mAdapter1.updateData(mList);
        } else if (mType == 2) {
            mAdapter2.updateData(mList);
        } else if (mType == 4) {
            mAdapter4.updateData(mList);
        }
    }

    public void loadMoreData() {
        pageIndex++;
        SPUserRequest.getMessageList(mType, pageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null && ((ArrayList) response).size() > 0) {
                    messageList = (List<SPMessageList>) response;
                    updateData(messageList);
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

}
