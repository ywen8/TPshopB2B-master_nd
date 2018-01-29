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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.person.order.SPOrderDetailActivity_;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPMessageList;
import com.tpshop.mall.model.SPMessageListData;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuhao
 */
public class SPMessageLogisticsAdapter extends SectionedRecyclerViewAdapter<SPMessageLogisticsAdapter.HeaderViewHolder,
        SPMessageLogisticsAdapter.OrderItemViewHolder, SPMessageLogisticsAdapter.FooterViewHolder> implements View.OnClickListener {

    private Context mContext;
    private Handler mHandler;
    private LayoutInflater mInflater;
    private static final String TYPE = "1";
    private List<SPMessageList> mMessageList;

    public SPMessageLogisticsAdapter(Context context, Handler handler) {
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
        View view = mInflater.inflate(R.layout.message_list_item_time_header_empty, parent, false);
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
        holder.shoppingTitleLl.setVisibility(View.GONE);
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder holder, int section) {
    }

    @Override
    protected void onBindItemViewHolder(OrderItemViewHolder holder, int section, int position) {
        final SPMessageListData messageListData = mMessageList.get(section).getMessageData();
        if (messageListData != null) {
            holder.titleText.setText(messageListData.getMessageDataTitle());
            holder.titleInfo.setText(messageListData.getMessageDataDiscription());
        } else {
            holder.titleText.setText("数据为null");
            holder.titleInfo.setText("数据为null");
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProductDetail(messageListData.getMessageDataGoodsId());
            }
        });
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, messageListData.getMessageDataGoodsId());
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.infoIv);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout shoppingTitleLl;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            shoppingTitleLl = ((LinearLayout) itemView.findViewById(R.id.shopping_title_ll));
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void gotoProductDetail(String orderID) {
        Intent detailIntent = new Intent(mContext, SPOrderDetailActivity_.class);
        detailIntent.putExtra("orderId", orderID);
        mContext.startActivity(detailIntent);
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
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
        }
    }

}
