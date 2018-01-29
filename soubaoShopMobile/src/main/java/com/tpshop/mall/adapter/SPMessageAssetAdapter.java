package com.tpshop.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.person.catipal.SPCapitalManageActivity_;
import com.tpshop.mall.activity.shop.SPProductShowListActivity_;
import com.tpshop.mall.model.SPMessageList;
import com.tpshop.mall.model.SPMessageListData;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuhao
 */
public class SPMessageAssetAdapter extends SectionedRecyclerViewAdapter<SPMessageAssetAdapter.HeaderViewHolder,
        SPMessageAssetAdapter.OrderItemViewHolder, SPMessageAssetAdapter.FooterViewHolder> implements View.OnClickListener {

    private Context mContext;
    private Handler mHandler;
    private LayoutInflater mInflater;
    private static final String TYPE = "4";
    private List<SPMessageList> mMessageList;

    public SPMessageAssetAdapter(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    public void updateData(List<SPMessageList> messageList) {
        if (messageList == null) {
            this.mMessageList = new ArrayList<>();
        } else {
            List<SPMessageList> messageListSwap = new ArrayList<>();
            for (int i = 0; i < messageList.size(); i++) {
                if (!SPStringUtils.isEmpty(messageList.get(i).getMessageType()) && TYPE.equals(messageList.get(i).getMessageType()))
                    messageListSwap.add(messageList.get(i));
            }
            this.mMessageList = messageList;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Message msg = null;
        mHandler.sendMessage(msg);
    }

    @Override
    protected int getSectionCount() {
        if (mMessageList == null) return 0;
        return mMessageList.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return 1;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.message_list_item_time_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    protected FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected OrderItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.message_list_item_asset, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        SPMessageListData messageListData = mMessageList.get(section).getMessageData();
        if (messageListData != null)
            holder.timeText.setText(messageListData.getTime());
        else
            holder.timeText.setText("数据为null");
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder holder, int section) {
    }

    @Override
    protected void onBindItemViewHolder(OrderItemViewHolder holder, int section, int position) {
        final SPMessageListData messageListData = mMessageList.get(section).getMessageData();
        if (messageListData != null) {
            holder.titleText.setText(messageListData.getMessageDataChangeType());
            holder.titleInfo.setText(messageListData.getMessageDataDiscription());
        } else {
            holder.titleText.setText("数据为null");
            holder.titleInfo.setText("数据为null");
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (messageListData.getMessageDataChangeType()) {
                    case "1":         //积分
                        Intent intent1 = new Intent(mContext, SPCapitalManageActivity_.class);
                        intent1.putExtra("goodsId", messageListData.getMessageDataGoodsId());
                        mContext.startActivity(intent1);
                        break;
                    case "2":         //余额
                        Intent intent2 = new Intent(mContext, SPCapitalManageActivity_.class);
                        intent2.putExtra("goodsId", messageListData.getMessageDataGoodsId());
                        mContext.startActivity(intent2);
                        break;
                    default:          //优惠价
                        Intent intent3 = new Intent(mContext, SPProductShowListActivity_.class);
                        intent3.putExtra("goodsId", messageListData.getMessageDataGoodsId());
                        mContext.startActivity(intent3);
                        break;
                }
            }
        });
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            timeText = ((TextView) itemView.findViewById(R.id.time_text));
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView infoTv;
        ImageView infoIv;
        CardView cardView;
        TextView titleText;
        TextView titleInfo;

        OrderItemViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            titleInfo = (TextView) itemView.findViewById(R.id.title_info);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            infoIv = (ImageView) itemView.findViewById(R.id.info_iv);
            infoTv = (TextView) itemView.findViewById(R.id.info_tv);
            infoTv.setVisibility(View.VISIBLE);
            infoIv.setVisibility(View.GONE);
        }
    }

}
