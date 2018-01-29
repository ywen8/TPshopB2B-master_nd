/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2127 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: zengwei
 * Date: @date 2017-6-7
 * Description: 分销中心 -》 分销订单
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.person.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPOrderBaseActivity;
import com.tpshop.mall.activity.shop.SPStoreHomeActivity_;
import com.tpshop.mall.adapter.ListDividerItemDecoration;
import com.tpshop.mall.adapter.SPOrderListAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.model.order.SPOrder;
import com.tpshop.mall.utils.SPConfirmDialog;
import com.tpshop.mall.widget.swipetoloadlayout.OnLoadMoreListener;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_spvirtual_order)
public class SPVirtualOrderActivity extends SPOrderBaseActivity implements SPConfirmDialog.ConfirmDialogListener, OnRefreshListener,
        OnLoadMoreListener, View.OnClickListener {

    @ViewById(R.id.all_order_txt)
    TextView allOrder;      //全部订单

    @ViewById(R.id.wait_pay_txt)
    TextView waitPay;       //待付款

    @ViewById(R.id.has_paid_txt)
    TextView hasPaid;       //已付款

    @ViewById(R.id.has_done_txt)
    TextView hasDone;       //已完成

    @ViewById(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;

    int mPageIndex = 1;
    private String type;                      //订单类型
    private List<SPOrder> orders;
    private SPOrder currentSelectOrder;       //选中的订单
    private SPOrderListAdapter mAdapter;
    private OrderChangeReceiver mReceiver;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SPMobileConstants.tagPay:       //支付订单
                    SPMobileApplication.getInstance().fellBack = 3;
                    SPOrder order = (SPOrder) msg.obj;
                    order.setVirtual(true);
                    gotoPay(order);
                    break;
                case SPMobileConstants.tagCancel:       //取消订单
                    currentSelectOrder = (SPOrder) msg.obj;
                    cancelOrder();
                    break;
                case SPMobileConstants.tagShopping:       //查看物流
                    lookShopping((SPOrder) msg.obj);
                    break;
                case SPMobileConstants.tagReceive:       //确认收货
                    confirmReceive((SPOrder) msg.obj);
                    break;
                case SPMobileConstants.tagReturn:
                    break;
                case SPMobileConstants.tagBuyAgain:       //联系客服
                    connectSaller((SPOrder) msg.obj);
                    break;
                case SPMobileConstants.MSG_CODE_ORDER_LIST_ITEM_ACTION:       //订单详情
                    Intent detailIntent = new Intent(SPVirtualOrderActivity.this, SPOrderDetailActivity_.class);
                    detailIntent.putExtra("isVirtual", true);
                    detailIntent.putExtra("orderId", ((SPOrder) msg.obj).getOrderID());
                    startActivity(detailIntent);
                    break;
                case SPMobileConstants.MSG_CODE_STORE_HOME_ACTION:       //店铺首页
                    Intent storeIntent = new Intent(SPVirtualOrderActivity.this, SPStoreHomeActivity_.class);
                    storeIntent.putExtra("storeId", Integer.parseInt(msg.obj.toString()));
                    startActivity(storeIntent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "虚拟订单");
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SPMobileConstants.ACTION_ORDER_CHANGE);         //监听订单状态变化
        filter.addAction(SPMobileConstants.ACTION_COMMENT_CHANGE);       //监听在评价页面评论完成
        mReceiver = new OrderChangeReceiver();
        registerReceiver(mReceiver, filter);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        View emptyView = findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        refreshRecyclerView.init(new LinearLayoutManager(this), this, this);
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        Drawable dividler = getResources().getDrawable(R.drawable.divider_grid_product_list);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(dividler));             //设置分割线
        mAdapter = new SPOrderListAdapter(this, mHandler);
        refreshRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        allOrder.setOnClickListener(this);
        waitPay.setOnClickListener(this);
        hasPaid.setOnClickListener(this);
        hasDone.setOnClickListener(this);
    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_order_txt:      //全部订单
                type = null;
                checkType(true, false, false, false);
                refreshData();
                break;
            case R.id.wait_pay_txt:       //待付款
                type = "WAITPAY";
                checkType(false, true, false, false);
                refreshData();
                break;
            case R.id.has_paid_txt:       //已付款
                type = "PAYED";
                checkType(false, false, true, false);
                refreshData();
                break;
            case R.id.has_done_txt:       //已完成
                type = "FINISH";
                checkType(false, false, false, true);
                refreshData();
                break;
        }
    }

    private void checkType(boolean a, boolean b, boolean c, boolean d) {
        if (a) {
            allOrder.setTextColor(getResources().getColor(R.color.light_red));
            waitPay.setTextColor(getResources().getColor(R.color.person_info_text));
            hasPaid.setTextColor(getResources().getColor(R.color.person_info_text));
            hasDone.setTextColor(getResources().getColor(R.color.person_info_text));
        } else if (b) {
            allOrder.setTextColor(getResources().getColor(R.color.person_info_text));
            waitPay.setTextColor(getResources().getColor(R.color.light_red));
            hasPaid.setTextColor(getResources().getColor(R.color.person_info_text));
            hasDone.setTextColor(getResources().getColor(R.color.person_info_text));
        } else if (c) {
            allOrder.setTextColor(getResources().getColor(R.color.person_info_text));
            waitPay.setTextColor(getResources().getColor(R.color.person_info_text));
            hasPaid.setTextColor(getResources().getColor(R.color.light_red));
            hasDone.setTextColor(getResources().getColor(R.color.person_info_text));
        } else if (d) {
            allOrder.setTextColor(getResources().getColor(R.color.person_info_text));
            waitPay.setTextColor(getResources().getColor(R.color.person_info_text));
            hasPaid.setTextColor(getResources().getColor(R.color.person_info_text));
            hasDone.setTextColor(getResources().getColor(R.color.light_red));
        }
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onLoadMore() {
        loadMoreData();
    }

    private void refreshData() {
        this.mPageIndex = 1;
        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(type))
            params.put("type", type);
        params.put("p", this.mPageIndex);
        showLoadingSmallToast();
        SPPersonRequest.getVirtualOrderList(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    orders = (List<SPOrder>) response;
                    mAdapter.updateData(orders);
                    refreshRecyclerView.showData();
                } else {
                    refreshRecyclerView.showEmpty();
                }
            }
        }, new SPFailuredListener(SPVirtualOrderActivity.this) {
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
        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(type))
            params.put("type", type);
        params.put("p", mPageIndex);
        SPPersonRequest.getVirtualOrderList(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    List<SPOrder> lists = (List<SPOrder>) response;
                    orders.addAll(lists);
                    mAdapter.updateData(orders);
                }
            }
        }, new SPFailuredListener(SPVirtualOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
                mPageIndex--;
            }
        });
    }

    /**
     * 联系客服
     */
    private void connectSaller(SPOrder mOrder) {
        String qq = mOrder.getStore().getStoreQQ();
        if (!SPStringUtils.isEmpty(qq))
            connectCustomer(qq);
        else
            showToast("暂无联系方式");
    }

    /**
     * 取消订单
     */
    public void cancelOrder() {
        showConfirmDialog("确定取消订单", "订单提醒", this, SPMobileConstants.tagCancel);
    }

    @Override
    public void clickOk(int actionType) {
        if (actionType == SPMobileConstants.tagCancel) {
            if (currentSelectOrder.getPayStatus() == 1) {      //取消已支付的订单
                showLoadingSmallToast();
                cancelOrder2(currentSelectOrder.getOrderID(), new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingSmallToast();
                        showSuccessToast(msg);
                        refreshData();
                        Intent intent = new Intent(SPMobileConstants.ACTION_ORDER_CHANGE);
                        sendBroadcast(intent);
                    }
                }, new SPFailuredListener(SPVirtualOrderActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingSmallToast();
                        showFailedToast(msg);
                    }
                });
            } else {      //取消未支付的订单
                showLoadingSmallToast();
                cancelOrder(currentSelectOrder.getOrderID(), new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingSmallToast();
                        showSuccessToast(msg);
                        refreshData();
                    }
                }, new SPFailuredListener(SPVirtualOrderActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingSmallToast();
                        showFailedToast(msg);
                    }
                });
            }
        }
    }

    /**
     * 确认收货
     */
    public void confirmReceive(SPOrder order) {
        showLoadingSmallToast();
        confirmOrderWithOrderID(order.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showSuccessToast(msg);
                refreshData();
            }
        }, new SPFailuredListener(SPVirtualOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SPMobileConstants.Result_Code_PAY:
                refreshData();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    class OrderChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SPMobileConstants.ACTION_ORDER_CHANGE)
                    || intent.getAction().equals(SPMobileConstants.ACTION_COMMENT_CHANGE))
                refreshData();
        }
    }

}
