package com.tpshop.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPProduct;

import java.util.List;

/**
 * Created by zw on 2017/5/26
 */
class ConfirmOrderProductAdapter extends BaseAdapter {

    private Context context;
    private boolean isIntegralGood;
    private List<SPProduct> products;
    private LayoutInflater layoutInflater;

    ConfirmOrderProductAdapter(Context context, List<SPProduct> products, boolean isIntegralGood) {
        this.context = context;
        this.products = products;
        this.isIntegralGood = isIntegralGood;
        this.layoutInflater = LayoutInflater.from(context);
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
            convertView = layoutInflater.inflate(R.layout.order_confirm_order_product_item, null);
            holder.productImgv = (ImageView) convertView.findViewById(R.id.product_pic_imgv);
            holder.exchangeTxtv = (TextView) convertView.findViewById(R.id.exchange_txtv);
            holder.productCutfeeTxtv = (TextView) convertView.findViewById(R.id.product_cutfee_txtv);
            holder.productNameTxtv = (TextView) convertView.findViewById(R.id.product_name_txtv);
            holder.productPriceTxtv = (TextView) convertView.findViewById(R.id.product_price_txtv);
            holder.productSpecTxtv = (TextView) convertView.findViewById(R.id.product_spec_txtv);
            holder.productCountTxtv = (TextView) convertView.findViewById(R.id.product_count_txtv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SPProduct product = products.get(position);
        holder.productNameTxtv.setText(product.getGoodsName());
        if (!SPStringUtils.isEmpty(product.getPromTitle())) {
            holder.productCutfeeTxtv.setText(product.getPromTitle());
            holder.productCutfeeTxtv.setVisibility(View.VISIBLE);
        } else {
            holder.productCutfeeTxtv.setVisibility(View.GONE);
        }
        holder.productCountTxtv.setText("x" + product.getGoodsNum());
        if (isIntegralGood) {
            holder.productPriceTxtv.setText(product.getMemberGoodsPrice());
            holder.exchangeTxtv.setText("不支持7天无理由退换货");
        } else {
            holder.productPriceTxtv.setText("¥" + product.getMemberGoodsPrice());
        }
        String specName = (product.getSpecKeyName() == null) ? "规格:无" : product.getSpecKeyName();
        holder.productSpecTxtv.setText(specName);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, 400, 400, product.getGoodsID());
        Glide.with(context).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.productImgv);
        return convertView;
    }

    class ViewHolder {
        ImageView productImgv;
        TextView productNameTxtv, exchangeTxtv, productCutfeeTxtv, productPriceTxtv, productSpecTxtv, productCountTxtv;
    }

}
