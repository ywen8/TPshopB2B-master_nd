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
 * Description:  团购商品列表
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.shop.SPIntegralMallActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPIntegralListAdapter extends RecyclerView.Adapter<SPIntegralListAdapter.ViewHolder> {

    private Context mContext;
    private List<SPProduct> mProducts;
    private OnItemClickListener mListener;

    public SPIntegralListAdapter(SPIntegralMallActivity activity) {
        this.mContext = activity;
        this.mListener = activity;
    }

    public void updateData(List<SPProduct> products) {
        if (products == null) products = new ArrayList<>();
        this.mProducts = products;
        this.notifyDataSetChanged();
    }

    @Override
    public SPIntegralListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.integral_list_item, parent, false);
        float height = mContext.getResources().getDimension(R.dimen.dp_110);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                Float.valueOf(height).intValue());
        layoutParams.setMargins(0, 2, 0, 0);
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SPIntegralListAdapter.ViewHolder holder, int position) {
        final SPProduct product = mProducts.get(position);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, String.valueOf(product.getGoodsID()));
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.goodsPicImgv);
        holder.productNameTxtv.setText(product.getGoodsName());
        holder.marketPriceTxtv.setText("¥" + product.getMarketPrice() + "元");        //市场价
        BigDecimal b1 = new BigDecimal(product.getShopPrice());
        BigDecimal b2 = new BigDecimal(String.valueOf(product.getExchangeIntegral()));
        BigDecimal b3 = new BigDecimal(product.getPointRate());
        SpannableString spanString;
        if ((b1.subtract(b2.divide(b3))).doubleValue() <= 0)
            spanString = getFmtPrice("0", String.valueOf((int) Math.ceil((b1.multiply(b3)).doubleValue())));
        else
            spanString = getFmtPrice(b1.subtract(b2.divide(b3)).toString(), String.valueOf(product.getExchangeIntegral()));
        if (spanString != null) holder.integralPriceTxtv.setText(spanString);
        holder.integralBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onItemClick(product.getGoodsID());
            }
        });
    }

    private SpannableString getFmtPrice(String price, String integral) {
        if (SPStringUtils.isEmpty(price) || SPStringUtils.isEmpty(integral)) return null;
        String fmtText;
        float smallFontSize = mContext.getResources().getDimension(R.dimen.textSizeSmall);
        SpannableString spanStr;
        if (price.equals("0")) {
            fmtText = integral + "积分";
            spanStr = new SpannableString(fmtText);
            spanStr.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.light_red)), 0, integral.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);         //设置区域内文字颜色为洋红色
            spanStr.setSpan(new AbsoluteSizeSpan(Float.valueOf(smallFontSize).intValue()), integral.length(), fmtText.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            fmtText = "¥" + price + "+" + integral + "积分";
            int redLength = price.length() + integral.length() + 2;
            spanStr = new SpannableString(fmtText);
            spanStr.setSpan(new AbsoluteSizeSpan(Float.valueOf(smallFontSize).intValue()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanStr.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.light_red)), 0, redLength,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);         //设置区域内文字颜色为洋红色
            spanStr.setSpan(new AbsoluteSizeSpan(Float.valueOf(smallFontSize).intValue()), fmtText.length() - 2, fmtText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spanStr;
    }

    @Override
    public int getItemCount() {
        if (mProducts == null) return 0;
        return mProducts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button integralBuyBtn;             //兑换
        ImageView goodsPicImgv;
        TextView productNameTxtv;
        TextView marketPriceTxtv;          //现价
        TextView integralPriceTxtv;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsPicImgv = (ImageView) itemView.findViewById(R.id.product_pic_imgv);
            productNameTxtv = (TextView) itemView.findViewById(R.id.product_name_txtv);
            integralPriceTxtv = (TextView) itemView.findViewById(R.id.integral_price_txtv);
            marketPriceTxtv = (TextView) itemView.findViewById(R.id.market_price_txtv);             //原价
            marketPriceTxtv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);                       //设置删除线
            integralBuyBtn = (Button) itemView.findViewById(R.id.integral_buy_btn);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String groupId);
    }

}
