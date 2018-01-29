package com.tpshop.mall.activity.person.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.BadgeView;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.ListDividerItemDecoration;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.model.SPMessageNotice;
import com.tpshop.mall.push.ExampleUtil;
import com.tpshop.mall.utils.SPUtils;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * @author liuhao
 */
@EActivity(R.layout.message_center)
public class SPMessageCenterActivity extends SPBaseActivity implements OnRefreshListener {

    @ViewById(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;

    int pageIndex;
    View emptyView;
    private Context mContext;
    public String[] mItemTile;
    private SPMessageSettingsActivity smsa;
    private MessageSettingsAdapter mAdapter;
    List<SPMessageNotice> messageNoticeList;
    private MessageReceiver mMessageReceiver;
    public static final String KEY_EXTRAS = "extras";
    public static final String KEY_MESSAGE = "message";
    public static final String MESSAGE_RECEIVED_ACTION = "com.tpshop.mall.MESSAGE_RECEIVED_ACTION";
    private int[] drawableTile = {
            R.drawable.msg_center_alert,
            R.drawable.msg_center_logistics,
            R.drawable.msg_center_sale,
            R.drawable.msg_center_shop,
            R.drawable.msg_center_asset
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.setCustomerTitleMore(true, getString(R.string.message_center_title));
        super.onCreate(arg0);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        mContext = this;
        Resources res = getResources();
        smsa = new SPMessageSettingsActivity();
        mItemTile = res.getStringArray(R.array.message_settings_item);       //0系統消息,1物流通知,2优惠促销,3商品提醒,4我的资产,5商城好店
        ImageButton settingBtn = (ImageButton) findViewById(R.id.titlebar_more_btn);
        settingBtn.setImageDrawable(this.getResources().getDrawable(R.drawable.msg_center_settings));
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SPMessageSettingsActivity_.class));
            }
        });
        emptyView = findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        refreshRecyclerView.init(new LinearLayoutManager(this), this, null);
        Drawable drawable = getResources().getDrawable(R.drawable.divider_grid_product_list);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(drawable));             //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(false);
    }

    @Override
    public void initEvent() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new MessageSettingsAdapter(this);
        refreshRecyclerView.setAdapter(mAdapter);
        reloadData();
        JPushInterface.onResume(this);
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private void reloadData() {
        registerMessageReceiver();
        refreshData();
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    private List<SPMessageNotice> filterData(List<SPMessageNotice> message) {
        boolean[] IsOn = smsa.getMessageSettingsEnableArray(mContext, SPMessageSettingsActivity.MESSAGE_SETTINGS_ENABLE_ARRAY);
        List<SPMessageNotice> listA = new ArrayList<>();        //数据过滤,先定义一个listA,把过滤数据放到listA里面,再吧过滤的数据listA赋值给list
        if (message != null) {
            for (int i = 0; i <= message.size() - 1; i++)
                for (int j = 0; j < mItemTile.length; j++) {
                    if (message.get(i).getMessageCategory().equals(j + ""))
                        if (IsOn[j]) listA.add(message.get(i));
                }
            message.clear();
            message = listA;
        }
        return message;
    }

    @Override
    public void onRefresh() {
        reloadData();
    }

    private void refreshData() {
        pageIndex = 1;
        showLoadingSmallToast();
        SPUserRequest.getMessageNotice(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    messageNoticeList = (List<SPMessageNotice>) response;
                    List<SPMessageNotice> filterData = filterData(messageNoticeList);
                    mAdapter.updateData(filterData);
                    refreshRecyclerView.showData();
                } else {
                    refreshRecyclerView.showEmpty();
                }
            }
        }, new SPFailuredListener(SPMessageCenterActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (!SPStringUtils.isEmpty(msg)) {
                    if (SPUtils.isTokenExpire(errorCode)) {
                        showToastUnLogin();
                        toLoginPage();
                    } else {
                        showFailedToast(msg);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private class MessageSettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context mContext;
        private LayoutInflater mInflater = null;
        private List<SPMessageNotice> mMessageNotice;

        public void updateData(List<SPMessageNotice> messageNoticeList) {
            if (messageNoticeList == null)
                this.mMessageNotice = new ArrayList<>();
            else
                this.mMessageNotice = messageNoticeList;
            this.notifyDataSetChanged();
        }

        private MessageSettingsAdapter(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.message_center_list_item, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            final ViewHolder holder = (ViewHolder) viewHolder;
            BadgeView badge = new BadgeView(mContext, holder.mallRl);
            fillingData(holder, position, badge);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int Type = -1;
                    TextView title = holder.title;
                    if (title != null) {
                        for (int i = 0; i < mItemTile.length; i++)
                            if (title.getText().equals(mItemTile[i])) Type = i;
                    }
                    Intent show = new Intent(SPMessageCenterActivity.this, SPMessageNoticeFragmentActivity_.class);
                    show.putExtra("fragmentIndex", Type);
                    startActivity(show);
                }
            });
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return mMessageNotice == null ? 0 : mMessageNotice.size();
        }

        private void fillingData(ViewHolder holder, int positon, BadgeView badge) {
            if (positon <= mMessageNotice.size()) {
                int Type = Integer.parseInt(mMessageNotice.get(positon).getMessageCategory());
                holder.title.setText(mItemTile[Type]);
                holder.info.setText(mMessageNotice.get(positon).getMessage());
                String mTime = "";
                if (mMessageNotice.get(positon).getMessageTime() != null)
                    mTime = SPUtils.getTimeFormPhpTime(Long.parseLong(mMessageNotice.get(positon).getMessageTime()));
                holder.time.setText(mTime);
                if (Type == 0) {
                    holder.image.setImageResource(drawableTile[0]);
                } else if (Type == 1) {
                    holder.image.setImageResource(drawableTile[1]);
                } else if (Type == 2) {
                    holder.image.setImageResource(drawableTile[2]);
                } else if (Type == 4) {
                    holder.image.setImageResource(drawableTile[4]);
                }
                if (mMessageNotice.get(positon).getMessageStatus().equals("0")) {
                    badge.setVisibility(View.GONE);
                } else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(mMessageNotice.get(positon).getMessageStatus());
                    badge.show();
                }
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;                        //标题
            public TextView time;                         //时间
            public TextView info;                         //消息的信息
            public TextView number;                       //消息个数
            public ImageView image;                       //头图片
            RelativeLayout mallRl;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.mall_dongdong_title);
                time = (TextView) itemView.findViewById(R.id.mall_dongdong_time);
                info = (TextView) itemView.findViewById(R.id.mall_dongdong_info);
                image = (ImageView) itemView.findViewById(R.id.mall_dongdong_iv);
                mallRl = (RelativeLayout) itemView.findViewById(R.id.mall_dongdong_rl);
            }
        }
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras))
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
            }
        }
    }

}
