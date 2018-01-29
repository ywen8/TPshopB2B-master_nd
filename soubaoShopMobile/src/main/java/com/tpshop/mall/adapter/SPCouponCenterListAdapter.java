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
 * Date: @date 2017年4月30日 下午10:03:56
 * Description:  领券中心 Adapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shinelw.library.ColorArcProgressBar;
import com.tpshop.mall.R;
import com.tpshop.mall.model.shop.SPCoupon;
import com.tpshop.mall.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPCouponCenterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    OnItemClickListener mListener;
    private List<SPCoupon> mCoupons;

    public SPCouponCenterListAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void updateData(List<SPCoupon> coupons) {
        if (coupons == null) coupons = new ArrayList<>();
        this.mCoupons = coupons;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.coupon_center_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        final SPCoupon coupon = mCoupons.get(position);
        Glide.with(mContext).load(SPUtils.getImageUrl(coupon.getImage())).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.couponPicImgv);
        holder.titleTxtv.setText("【" + coupon.getName() + "】" + coupon.getTitle());
        holder.moneyTxtv.setText(coupon.getMoney());
        int condition = Double.valueOf(coupon.getCondition()).intValue();
        holder.conditionTxtv.setText("满" + condition + "可用");      //团购价
        if (coupon.getIsget() == 1) {       //已经领取,去使用
            holder.useView.setVisibility(View.VISIBLE);
            holder.getView.setVisibility(View.GONE);
            holder.obtainImgv.setVisibility(View.VISIBLE);
        } else {        //还未领取,显示领取进度
            holder.useView.setVisibility(View.GONE);
            holder.getView.setVisibility(View.VISIBLE);
            holder.obtainImgv.setVisibility(View.GONE);
            holder.arcBar.setMaxValues(100);
            holder.arcBar.setDiameter(60);                        //设置直径
            holder.arcBar.setTextSize(13);                        //提示文字大小
            holder.arcBar.setBgArcColor("#48b3b5");               //进度条背景色
            holder.arcBar.invidateView();
            if (coupon.getCreateNum() <= 0) {
                holder.arcBar.setCurrentValues(0);
            } else {
                int curVal = coupon.getSendNum() * 100 / coupon.getCreateNum();
                holder.arcBar.setCurrentValues(curVal);
            }
        }
        holder.integralBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && coupon.getIsget() != 1)
                    mListener.onGetItemClick(coupon);
            }
        });
        holder.useCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && coupon.getIsget() == 1)
                    mListener.onCostItemClick(coupon);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCoupons == null) return 0;
        return mCoupons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View getView;                          //领取面板
        View useView;                          //使用面板
        TextView titleTxtv;
        TextView moneyTxtv;                    //金额
        ImageView obtainImgv;                  //已经获取
        ImageView useCouponBtn;                //使用
        TextView conditionTxtv;                //使用条件
        ImageView couponPicImgv;
        ImageView integralBuyBtn;              //领取
        ColorArcProgressBar arcBar;

        public ViewHolder(View itemView) {
            super(itemView);
            getView = itemView.findViewById(R.id.coupon_progress_rlayout);
            useView = itemView.findViewById(R.id.coupon_use_rlayout);
            obtainImgv = (ImageView) itemView.findViewById(R.id.obtain_imgv);
            useCouponBtn = (ImageView) itemView.findViewById(R.id.use_coupon_btn);
            couponPicImgv = (ImageView) itemView.findViewById(R.id.coupon_pic_imgv);
            titleTxtv = (TextView) itemView.findViewById(R.id.title_txtv);
            moneyTxtv = (TextView) itemView.findViewById(R.id.money_txtv);
            conditionTxtv = (TextView) itemView.findViewById(R.id.condition_txtv);
            integralBuyBtn = (ImageView) itemView.findViewById(R.id.integral_buy_btn);
            arcBar = (ColorArcProgressBar) itemView.findViewById(R.id.arc_bar);
        }
    }

    public interface OnItemClickListener {

        void onGetItemClick(SPCoupon coupon);               //领取

        void onCostItemClick(SPCoupon coupon);              //使用

    }

}
