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
 * Date: @date 2015年10月30日 下午10:03:56
 * Description:  促销/抢购/秒杀 商品列表
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
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
import com.tpshop.mall.model.shop.SPFlashSale;
import com.tpshop.mall.model.shop.SPFlashTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPFlashSaleListAdapter extends RecyclerView.Adapter<SPFlashSaleListAdapter.ViewHolder> {

    private Context mContext;
    private SPFlashTime mFlashTime;
    private OnItemClickListener mListener;
    private List<SPFlashSale> mFlashSales;

    public SPFlashSaleListAdapter(Context context, SPFlashTime flashTime, OnItemClickListener listener) {
        this.mContext = context;
        this.mFlashTime = flashTime;
        this.mListener = listener;
    }

    public void setData(List<SPFlashSale> flashSales) {
        if (flashSales == null)
            flashSales = new ArrayList<>();
        this.mFlashSales = flashSales;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flash_sale_list_item, parent, false);
        return new SPFlashSaleListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SPFlashSale flashSale = mFlashSales.get(position);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, flashSale.getGoodsId());
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv);
        holder.productNameTxtv.setText(flashSale.getGoodsName());
        holder.priceTxtv.setText("¥" + flashSale.getPrice());
        holder.goodsPriceTxtv.setText("¥" + flashSale.getShopPrice());
        holder.percentTxtv.setText("已抢" + flashSale.getPercent() + "%");
        if (mFlashTime.getType() == 1) {
            holder.buyTxtv.setBackgroundColor(mContext.getResources().getColor(R.color.light_red));
            holder.buyTxtv.setText(mContext.getString(R.string.flash_sale));
        } else {
            holder.buyTxtv.setBackgroundColor(mContext.getResources().getColor(R.color.light_green));
            holder.buyTxtv.setText(mContext.getString(R.string.go_buy));
        }
        holder.buyTxtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onItemClick(flashSale.getGoodsId(), flashSale.getItemId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mFlashSales == null) return 0;
        return mFlashSales.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView buyTxtv;
        ImageView picImgv;
        TextView priceTxtv;            //现价
        TextView percentTxtv;
        TextView goodsPriceTxtv;       //原价
        TextView productNameTxtv;

        public ViewHolder(View itemView) {
            super(itemView);
            picImgv = (ImageView) itemView.findViewById(R.id.product_pic_imgv);
            productNameTxtv = (TextView) itemView.findViewById(R.id.product_name_txtv);
            priceTxtv = (TextView) itemView.findViewById(R.id.price_txtv);
            goodsPriceTxtv = (TextView) itemView.findViewById(R.id.goods_price_txtv);
            goodsPriceTxtv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            percentTxtv = (TextView) itemView.findViewById(R.id.percent_txtv);
            buyTxtv = (TextView) itemView.findViewById(R.id.buy_txtv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String goodsId, String itemId);
    }

}
