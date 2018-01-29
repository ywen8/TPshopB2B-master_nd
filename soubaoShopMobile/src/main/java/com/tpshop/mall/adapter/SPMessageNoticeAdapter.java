package com.tpshop.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.shop.SPProductDetailActivity_;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPMessageList;
import com.tpshop.mall.model.SPMessageListData;
import com.tpshop.mall.widget.SPArrowRowView;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.tpshop.mall.R.id.sub_content_lv;

/**
 * @author liuhao
 */
public class SPMessageNoticeAdapter extends SectionedRecyclerViewAdapter<SPMessageNoticeAdapter.HeaderViewHolder,
        SPMessageNoticeAdapter.OrderItemViewHolder, SPMessageNoticeAdapter.FooterViewHolder> implements View.OnClickListener {

    private Context mContext;
    private Handler mHandler;
    private LayoutInflater mInflater;
    private static final String TYPE = "2";
    private List<SPMessageList> mMessageList;

    public SPMessageNoticeAdapter(Context context, Handler handler) {
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
        if (mMessageList == null)
            return 0;
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
        View view = mInflater.inflate(R.layout.message_list_item_content, parent, false);
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
        contentAdapter cAdapter = new contentAdapter(messageListData);
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
        holder.personFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProductDetail(messageListData.getMessageDataGoodsId());
            }
        });
        holder.contenLv.setAdapter(cAdapter);
        holder.contenLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoProductDetail(messageListData.getMessageDataGoodsId());
            }
        });
        setListViewHeightBasedOnChildren(holder.contenLv);
        if (messageListData.getMessageDataCover() != null && messageListData.getMessageDataCover().length() != 0)
            holder.productImgv.setImageURI(Uri.parse(messageListData.getMessageDataCover()));
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
        ListView contenLv;
        CardView cardView;
        TextView titleInfo;
        TextView titleText;
        ImageView productImgv;
        SPArrowRowView personFrameLayout;

        OrderItemViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            titleInfo = (TextView) itemView.findViewById(R.id.title_info);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            productImgv = (ImageView) itemView.findViewById(R.id.product_pic_imgv);
            contenLv = (ListView) itemView.findViewById(sub_content_lv);
            personFrameLayout = (SPArrowRowView) itemView.findViewById(R.id.person_system_setting);
        }
    }

    private void gotoProductDetail(String GoodsId) {
        Intent intent = new Intent(mContext, SPProductDetailActivity_.class);
        intent.putExtra("goodsID", GoodsId);
        mContext.startActivity(intent);
    }

    private class contentAdapter extends BaseAdapter {
        SPMessageListData mMessageListData = new SPMessageListData();

        private contentAdapter(SPMessageListData messageListData) {
            this.mMessageListData = messageListData;
        }

        @Override
        public int getCount() {
            return mMessageListData == null ? 0 : 1;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ContentViewHolder holder;
            if (convertView == null) {
                holder = new ContentViewHolder();
                convertView = mInflater.inflate(R.layout.message_list_item_content_item2, null);
                holder.contentTitle = (TextView) convertView.findViewById(R.id.msg_title);
                holder.contentImg = (ImageView) convertView.findViewById(R.id.msg_imgv);
                convertView.setTag(holder);
            } else {
                holder = (ContentViewHolder) convertView.getTag();
            }
            if (mMessageListData != null)
                holder.contentTitle.setText(mMessageListData.getMessageDataDiscription());
            else
                holder.contentTitle.setText("数据为空");
            if (mMessageListData.getMessageDataGoodsId() != null) {
                String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, mMessageListData.getMessageDataGoodsId());
                Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.contentImg);
            }
            return convertView;
        }
    }

    private class ContentViewHolder {
        TextView contentTitle;
        ImageView contentImg;
    }

}
