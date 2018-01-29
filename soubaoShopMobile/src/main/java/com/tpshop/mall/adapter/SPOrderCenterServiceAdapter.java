package com.tpshop.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.shop.SPProductDetailActivity_;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPServiceComment;
import com.tpshop.mall.model.shop.SPServiceCommentList;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SPOrderCenterServiceAdapter extends SectionedRecyclerViewAdapter<SPOrderCenterServiceAdapter.HeaderViewHolder,
        SPOrderCenterServiceAdapter.OrderItemViewHolder, SPOrderCenterServiceAdapter.FooterViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mListener;
    private List<SPServiceCommentList> mServiceCommentList;

    public SPOrderCenterServiceAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void updateData(List<SPServiceCommentList> serviceCommentList) {
        if (serviceCommentList == null)
            serviceCommentList = new ArrayList<>();
        this.mServiceCommentList = serviceCommentList;
        this.notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        if (mServiceCommentList == null) return 0;
        return mServiceCommentList.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (mServiceCommentList == null || mServiceCommentList.get(section).getServiceCommentDate() == null)
            return 0;
        return mServiceCommentList.get(section).getServiceCommentDate().size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.order_list_item_service_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    protected FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.order_list_item_service_footer, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    protected OrderItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.order_list_item_service_details, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        SPServiceCommentList commentList = mServiceCommentList.get(section);
        String name = (SPStringUtils.isEmpty(commentList.getStoreName())) ? "店铺名称异常" : commentList.getStoreName();
        holder.headerName.setText(name);
        holder.headerName.setTag(commentList.getServiceCommentDate().get(0).getStoreId());
        holder.headerStoreImg.setTag(commentList.getServiceCommentDate().get(0).getStoreId());
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder holder, int section) {
        final SPServiceCommentList commentList = mServiceCommentList.get(section);
        holder.footerBtnRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onItemClick(commentList.getServiceCommentDate().get(0).getOrderId());
            }
        });
        holder.footerBtnRelativeLayout.setTag(section);
        holder.footerBtnText.setText("服务评价");
        holder.footerDescribe.setText("完成最多获得10金币");
    }

    @Override
    protected void onBindItemViewHolder(OrderItemViewHolder holder, int section, int position) {
        final SPServiceComment comment = mServiceCommentList.get(section).getServiceCommentDate().get(position);
        holder.DetailsText.setText(comment.getGoodsName());
        holder.DetailsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProductDetail(comment, mContext);
            }
        });
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, comment.getGoodsId());
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.DetailsImg);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerName;               //商店名称
        ImageView headerStoreImg;          //商品图片

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerStoreImg = (ImageView) itemView.findViewById(R.id.store_iv);          //图片
            headerName = (TextView) itemView.findViewById(R.id.store_name_txtv);        //商店名称
        }
    }

    private void gotoProductDetail(SPServiceComment comment, Context context) {
        Intent intent = new Intent(context, SPProductDetailActivity_.class);
        intent.putExtra("goodsID", comment.getGoodsId());
        context.startActivity(intent);
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView footerBtnText;
        ImageView footerBtnImg;                         //商品图片
        TextView footerDescribe;                        //底部描述
        RelativeLayout footerBtnRelativeLayout;         //按钮布局

        public FooterViewHolder(View itemView) {
            super(itemView);
            footerBtnRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.btn_img_rl);
            footerDescribe = (TextView) itemView.findViewById(R.id.btn_describe);
            footerBtnImg = (ImageView) itemView.findViewById(R.id.btn_img);
            footerBtnText = (TextView) itemView.findViewById(R.id.order_show_btn);
        }
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView DetailsImg;                //商品图片
        TextView DetailsText;                //商品描述
        LinearLayout DetailsLinear;          //商品描述

        OrderItemViewHolder(View itemView) {
            super(itemView);
            DetailsImg = (ImageView) itemView.findViewById(R.id.product_pic_imgv);
            DetailsText = (TextView) itemView.findViewById(R.id.product_name_txtv);
            DetailsLinear = (LinearLayout) itemView.findViewById(R.id.order_button_layout);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String OrderId);
    }

}
