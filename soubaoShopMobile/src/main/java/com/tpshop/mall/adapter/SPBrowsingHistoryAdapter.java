package com.tpshop.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPBrowItem;
import com.tpshop.mall.model.SPBrowingProduct;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2017/5/23
 */
public class SPBrowsingHistoryAdapter extends SectionedRecyclerViewAdapter<SPBrowsingHistoryAdapter.HeaderViewHolder,
        SPBrowsingHistoryAdapter.HistoryItemViewHolder, SPBrowsingHistoryAdapter.FooterViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mListener;
    private List<SPBrowingProduct> product;

    public SPBrowsingHistoryAdapter(Context ctx, OnItemClickListener listener) {
        this.mContext = ctx;
        this.mListener = listener;
    }

    public void updateBrowData(List<SPBrowingProduct> product) {
        if (product == null)
            product = new ArrayList<>();
        this.product = product;
        this.notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        if (product != null) return product.size();
        return 0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (product != null && product.get(section).getVisitList() != null)
            return product.get(section).getVisitList().size();
        return 0;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.browing_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    protected FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected HistoryItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.browing_item, parent, false);
        return new HistoryItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        SPBrowingProduct browingProduct = product.get(section);
        holder.browTime.setTag(section);
        if (section > 1) {
            if (product.get(section - 1).getDate().equals(browingProduct.getDate())) {
                holder.browTime.setVisibility(View.GONE);
                holder.browTimeLine.setVisibility(View.GONE);
            } else {
                holder.browTime.setText(browingProduct.getDate());
                holder.browTime.setVisibility(View.VISIBLE);
                holder.browTimeLine.setVisibility(View.VISIBLE);
            }
        } else {
            holder.browTime.setText(browingProduct.getDate());
            holder.browTime.setVisibility(View.VISIBLE);
            holder.browTimeLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder holder, int section) {
    }

    @Override
    protected void onBindItemViewHolder(HistoryItemViewHolder holder, int section, int position) {
        SPBrowingProduct item = product.get(section);
        final SPBrowItem product = item.getVisitList().get(position);
        holder.goodName.setText(product.getGoodsName());
        String price = "¥" + product.getShopPrice();
        SpannableString priceSpanStr = new SpannableString(price);
        float fontSize = this.mContext.getResources().getDimension(R.dimen.textSizeSmall);
        priceSpanStr.setSpan(new RelativeSizeSpan(fontSize), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.goodPrice.setText(price);
        holder.goodPrice.setTag(product);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, 400, 400, product.getGoodsID() + "");
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.goodImg);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView browTime;
        View browTimeLine;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            browTime = (TextView) itemView.findViewById(R.id.brow_time);
            browTimeLine = itemView.findViewById(R.id.brow_time_line);
        }
    }

    class HistoryItemViewHolder extends RecyclerView.ViewHolder {
        Button browBtn;
        ImageView goodImg;
        TextView goodName;
        TextView goodPrice;

        HistoryItemViewHolder(View itemView) {
            super(itemView);
            goodImg = (ImageView) itemView.findViewById(R.id.goodImg);
            goodName = (TextView) itemView.findViewById(R.id.goodName);
            goodPrice = (TextView) itemView.findViewById(R.id.goodPrice);
            browBtn = (Button) itemView.findViewById(R.id.browBtn);
            goodImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        SPBrowItem spBrowItem = (SPBrowItem) goodPrice.getTag();
                        mListener.onItemClick(spBrowItem);
                    }
                }
            });
            browBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SPBrowItem spBrowItem = (SPBrowItem) goodPrice.getTag();
                    int catId = spBrowItem.getCategoryID();
                    mListener.onSimilarClick(catId);
                }
            });
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(SPBrowItem item);

        void onSimilarClick(int catId);

    }

}
