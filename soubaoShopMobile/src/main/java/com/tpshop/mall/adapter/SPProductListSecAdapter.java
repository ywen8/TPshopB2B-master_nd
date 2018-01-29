/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年10月26日 下午9:52:45
 * Description: 商城 -> 首页 adapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPProduct;

import java.util.List;

public class SPProductListSecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SPProduct> mDatas;
    private OnItemClickListener mListener;

    public SPProductListSecAdapter(Context ctx, OnItemClickListener listener) {
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
        View view = mInflater.inflate(R.layout.product_list_recy_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder superHolder, int position) {
        ViewHolder holder = (ViewHolder) superHolder;
        SPProduct product = mDatas.get(position);
        holder.product = product;
        holder.nameTxtv.setText(product.getGoodsName());
        String price = "¥" + product.getShopPrice();
        SpannableString priceSpanStr = new SpannableString(price);
        float fontSize = this.mContext.getResources().getDimension(R.dimen.textSizeSmall);
        priceSpanStr.setSpan(new RelativeSizeSpan(fontSize), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.priceTxtv.setText(price);
        holder.commentTv.setText(product.getCommentCount() + "条评论");
        if (product.getCommentRate() != null)
            holder.commentRateTv.setText(product.getCommentRate() + "%好评");
        else
            holder.commentRateTv.setVisibility(View.GONE);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, 400, 400, product.getGoodsID());
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv);
    }

    @Override
    public int getItemCount() {
        if (this.mDatas == null) return 0;
        return this.mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picImgv;
        SPProduct product;
        TextView nameTxtv;                //商品名称
        TextView priceTxtv;               //商品价格
        TextView commentTv;               //评论数
        TextView commentRateTv;           //好评率

        public ViewHolder(View itemView) {
            super(itemView);
            nameTxtv = (TextView) itemView.findViewById(R.id.product_name_txtv);
            priceTxtv = (TextView) itemView.findViewById(R.id.product_price_txtv);
            picImgv = (ImageView) itemView.findViewById(R.id.product_pic_imgv);
            commentTv = (TextView) itemView.findViewById(R.id.comment_tv);
            commentRateTv = (TextView) itemView.findViewById(R.id.comment_rate_tv);
            picImgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onItemClick(product);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SPProduct product);
    }

}
