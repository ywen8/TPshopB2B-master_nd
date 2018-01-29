package com.tpshop.mall.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.model.SPMessageList;
import com.tpshop.mall.model.SPMessageListData;
import com.tpshop.mall.utils.SPDialogUtils;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuhao
 */
public class SPMessageServiceAdapter extends SectionedRecyclerViewAdapter<SPMessageServiceAdapter.HeaderViewHolder,
        SPMessageServiceAdapter.OrderItemViewHolder, SPMessageServiceAdapter.FooterViewHolder> implements View.OnClickListener {

    private Context mContext;
    private Handler mHandler;
    private LayoutInflater mInflater;
    private static final String TYPE = "0";
    private List<SPMessageList> mMessageList;

    public SPMessageServiceAdapter(Context context, Handler handler) {
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
        View view = mInflater.inflate(R.layout.message_list_item_service, parent, false);
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
        SPMessageListData messageListData = mMessageList.get(section).getMessageData();
        if (messageListData.getMessageDataDiscription().length() != 0)
            holder.titleInfo.setText(Html.fromHtml(messageListData.getMessageDataDiscription()));
        else
            holder.titleInfo.setText("数据为null");
        if (!SPStringUtils.isEmpty(messageListData.getMessageDataTitle()) && messageListData.getMessageDataTitle().length() != 0)
            holder.titleText.setText(messageListData.getMessageDataTitle());
        else
            holder.titleText.setText("数据为null");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPDialogUtils.showToast(mContext, "进入未支付订单");
            }
        });
        if (section % 2 == 0) {            //模拟该行消息失效
            holder.loseMessage.setVisibility(View.VISIBLE);
            holder.titleText.setTextColor(mContext.getResources().getColor(R.color.gray));
            holder.titleInfo.setTextColor(mContext.getResources().getColor(R.color.gray));
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            timeText = (TextView) itemView.findViewById(R.id.time_text);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        CardView cardView;
        TextView titleInfo;
        ImageView loseMessage;

        OrderItemViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            titleInfo = (TextView) itemView.findViewById(R.id.title_info);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            loseMessage = (ImageView) itemView.findViewById(R.id.lose_message_image);
        }
    }

}
