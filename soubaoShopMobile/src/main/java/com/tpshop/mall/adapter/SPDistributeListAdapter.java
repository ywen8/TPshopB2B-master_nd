package com.tpshop.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.widget.SPBorderImageView;

import java.util.List;

/**
 * Created by zw on 2017/6/15
 */
public class SPDistributeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SPProduct> mDatas;
    private OnItemClickListener mListener;

    public SPDistributeListAdapter(Context ctx, OnItemClickListener listener) {
        this.mContext = ctx;
        this.mListener = listener;
    }

    public void updateData(List<SPProduct> products) {
        if (products == null) return;
        this.mDatas = products;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.distribute_list_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder superHolder, int position) {
        ViewHolder holder = (ViewHolder) superHolder;
        SPProduct product = mDatas.get(position);
        holder.nameTxtv.setText(product.getGoodsName());
        String price = "¥" + product.getShopPrice();
        holder.shopPrice.setText(price);
        holder.priceTxtv.setText("分成金额：" + product.getDistribute());
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, 400, 400, product.getGoodsID());
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv);
        if (product.getSelected().equals("1"))
            holder.checkBtn.setBackgroundResource(R.drawable.icon_checked);
        else
            holder.checkBtn.setBackgroundResource(R.drawable.icon_checkno);
        holder.checkBtn.setTag(product);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button checkBtn;
        TextView nameTxtv;                 //商品名称
        TextView priceTxtv;                //分成金额
        TextView shopPrice;                //评论数
        SPBorderImageView picImgv;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBtn = (Button) itemView.findViewById(R.id.check_btn);
            picImgv = (SPBorderImageView) itemView.findViewById(R.id.pic_imgv);
            nameTxtv = (TextView) itemView.findViewById(R.id.name_txtv);
            priceTxtv = (TextView) itemView.findViewById(R.id.product_spec_txtv);
            shopPrice = (TextView) itemView.findViewById(R.id.shop_price_txtv);
            picImgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getLayoutPosition();
                        mListener.onItemClick(mDatas.get(position));
                    }
                }
            });
            nameTxtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getLayoutPosition();
                        mListener.onItemClick(mDatas.get(position));
                    }
                }
            });
            checkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SPProduct item = (SPProduct) checkBtn.getTag();
                    boolean checked = !item.getSelected().equals("1");
                    if (mListener != null)
                        mListener.checkProductFromCart(item, checked);
                }
            });
        }
    }

    public interface OnItemClickListener {

        void onItemClick(SPProduct product);

        void checkProductFromCart(SPProduct product, boolean checked);

    }

}
