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
 * Description: 购物车 -> 订单填写页面
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.widget.NoScrollListView;
import com.tpshop.mall.widget.SPArrowRowView;

import java.util.ArrayList;
import java.util.List;

public class SPConfirmOrderAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context mContext;
    private List<SPStore> mStores;
    private boolean isIntegralGood;
    private LayoutInflater mLayoutInflater;
    private SPConfirmOrderListener mListener;

    public SPConfirmOrderAdapter(Context context, SPConfirmOrderListener listener, boolean isIntegralGood) {
        mContext = context;
        this.mListener = listener;
        this.isIntegralGood = isIntegralGood;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<SPStore> stores) {
        if (stores == null)
            stores = new ArrayList<>();
        this.mStores = stores;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mLayoutInflater.inflate(R.layout.order_confirm_order_item, null, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SPStore store = mStores.get(position);
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.storeNameTxtv.setText(store.getStoreName());
        if (!SPStringUtils.isEmpty(store.getPromTitle()))
            viewHolder.storePromTitle.setText(store.getPromTitle());
        else
            viewHolder.storePromTitle.setText("无");
        viewHolder.storeFeeTv.setText(store.getShipfee());
        viewHolder.couponArv.setOnClickListener(this);
        viewHolder.couponArv.setTag(store);
        viewHolder.selerMessageArv.setOnClickListener(this);
        viewHolder.selerMessageArv.setTag(store);
        List<SPProduct> products = store.getStoreProducts();
        ConfirmOrderProductAdapter adapter = new ConfirmOrderProductAdapter(mContext, products, isIntegralGood);
        viewHolder.productLayout.setAdapter(adapter);
        if (!isIntegralGood) {
            if (store.getSelectedCoupon() != null)
                viewHolder.couponArv.setSubText(store.getSelectedCoupon().getName());
            else
                viewHolder.couponArv.setSubText("");
            viewHolder.couponArv.setSecText(store.getCouponNum() + "张优惠券");
        }
        if (store.getSelerMessage() == null || store.getSelerMessage().equals(""))
            viewHolder.selerMessageArv.setSubText(mContext.getString(R.string.send_seler_message));
        else
            viewHolder.selerMessageArv.setSubText(store.getSelerMessage());
    }

    @Override
    public int getItemCount() {
        if (mStores == null) return 0;
        return mStores.size();
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) return;
        if (!(v.getTag() instanceof SPStore)) return;
        SPStore store = (SPStore) v.getTag();
        switch (v.getId()) {
            case R.id.order_coupon_aview:
                mListener.coupontItemClick(store);
                break;
            case R.id.order_seler_message_aview:
                mListener.selerMessageItemClick(store);
                break;
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView storeFeeTv;
        private TextView storeNameTxtv;
        private TextView storePromTitle;
        private SPArrowRowView couponArv;
        private NoScrollListView productLayout;
        private SPArrowRowView selerMessageArv;

        ItemViewHolder(View itemView) {
            super(itemView);
            productLayout = (NoScrollListView) itemView.findViewById(R.id.order_product_layout);
            storeNameTxtv = (TextView) itemView.findViewById(R.id.store_name_txtv);
            storePromTitle = (TextView) itemView.findViewById(R.id.store_prom_title_tv);
            storeFeeTv = (TextView) itemView.findViewById(R.id.store_fee_tv);
            couponArv = (SPArrowRowView) itemView.findViewById(R.id.order_coupon_aview);
            selerMessageArv = (SPArrowRowView) itemView.findViewById(R.id.order_seler_message_aview);
            selerMessageArv.setSubText(mContext.getString(R.string.send_seler_message));
            if (isIntegralGood)
                couponArv.setVisibility(View.GONE);
        }
    }

    public interface SPConfirmOrderListener {

        void coupontItemClick(SPStore store);                     //优惠券

        void selerMessageItemClick(SPStore store);                //买家留言

    }

}
