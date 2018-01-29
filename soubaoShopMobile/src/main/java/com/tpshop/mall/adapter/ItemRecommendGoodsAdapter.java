package com.tpshop.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.model.SPProduct;

import java.util.List;

/**
 * item页底部推荐商品适配器
 */
class ItemRecommendGoodsAdapter extends BaseAdapter {

    private Context context;
    private List<SPProduct> data;
    private LayoutInflater inflater;

    ItemRecommendGoodsAdapter(Context context, List<SPProduct> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<SPProduct> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<SPProduct> getData() {
        return this.data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_recommend_goods_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SPProduct recommendGood = data.get(position);
        holder.tv_goods_name.setText(recommendGood.getGoodsName());
        holder.tv_goods_price.setText("¥" + recommendGood.getShopPrice());
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, 500, 500, recommendGood.getGoodsID());
        Glide.with(context).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.sdv_goods);
        holder.sdv_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goodsId = recommendGood.getGoodsID();
                Intent detailIntent = new Intent(SPMobileConstants.ACTION_GOODS_RECOMMEND);
                Bundle bundle = new Bundle();
                bundle.putString("goods_id ", goodsId);
                detailIntent.putExtras(bundle);
                SPMobileApplication.getInstance().data = goodsId;
                SPMobileApplication.getInstance().sendBroadcast(detailIntent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView sdv_goods;
        private TextView tv_goods_name, tv_goods_price;

        public ViewHolder(View convertView) {
            sdv_goods = (ImageView) convertView.findViewById(R.id.product_imgv);
            tv_goods_name = (TextView) convertView.findViewById(R.id.product_name_txtv);
            tv_goods_price = (TextView) convertView.findViewById(R.id.product_price_txtv);
        }
    }

}
