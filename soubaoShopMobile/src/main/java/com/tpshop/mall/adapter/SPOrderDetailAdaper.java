package com.tpshop.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.shop.SPProductDetailActivity_;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.order.SPOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2017/6/7
 */
public class SPOrderDetailAdaper extends BaseAdapter {

    private SPOrder mOrder;
    private Context mContext;
    private List<SPProduct> products;
    private OnProductClickListener mListener;

    public SPOrderDetailAdaper(Context context, SPOrder order, List<SPProduct> products, OnProductClickListener listener) {
        this.mContext = context;
        this.mOrder = order;
        if (products == null)
            products = new ArrayList<>();
        this.products = products;
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_detail_product_item, null, false);
            holder.productImgv = (ImageView) convertView.findViewById(R.id.product_pic_imgv);
            holder.productNameTxtv = (TextView) convertView.findViewById(R.id.product_name_txtv);
            holder.productPriceTxtv = (TextView) convertView.findViewById(R.id.product_price_txtv);
            holder.productSpecTxtv = (TextView) convertView.findViewById(R.id.product_spec_txtv);
            holder.productCountTxtv = (TextView) convertView.findViewById(R.id.product_count_txtv);
            holder.applyReturnBtn = (Button) convertView.findViewById(R.id.product_apply_return_btn);
            holder.commentBtn = (Button) convertView.findViewById(R.id.product_apply_comment_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SPProduct product = products.get(position);
        String specName = SPStringUtils.isEmpty(product.getSpecKeyName()) ? "规格:无" : product.getSpecKeyName();
        holder.productNameTxtv.setText(product.getGoodsName());
        holder.productCountTxtv.setText("x" + product.getGoodsNum());
        holder.productPriceTxtv.setText("¥" + product.getGoodsPrice());
        holder.productSpecTxtv.setText(specName);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, product.getGoodsID());
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.productImgv);
        if (mOrder.getButtom().getReturnBtn() == 1 && product.getIsSend() == 1)          //订单商品可以申请售后
            holder.applyReturnBtn.setVisibility(View.VISIBLE);
        else
            holder.applyReturnBtn.setVisibility(View.GONE);
        if (mOrder.getOrderStatus() == 2 && product.getIsComment() == 0) {         //订单商品可以评价
            holder.commentBtn.setVisibility(View.VISIBLE);
            holder.commentBtn.setTag(product);
        } else {
            holder.commentBtn.setVisibility(View.GONE);
        }
        holder.applyReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.applyReturn(product);
            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.commentProduct(product);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SPProductDetailActivity_.class);
                intent.putExtra("goodsID", product.getGoodsID());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        Button commentBtn;
        ImageView productImgv;
        Button applyReturnBtn;
        TextView productNameTxtv;
        TextView productSpecTxtv;
        TextView productPriceTxtv;
        TextView productCountTxtv;
    }

    public interface OnProductClickListener {

        void applyReturn(SPProduct product);

        void commentProduct(SPProduct product);

    }

}
