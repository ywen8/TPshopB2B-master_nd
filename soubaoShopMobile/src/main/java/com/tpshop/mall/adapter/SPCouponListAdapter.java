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
 * Description: 我的 -> 优惠券列表
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.model.shop.SPCoupon;
import com.tpshop.mall.utils.SPUtils;

import java.util.List;

/**
 * @author 飞龙
 */
public class SPCouponListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mType;           //优惠券类型
    private Context mContext;
    private List<SPCoupon> mCoupons;
    private ItemClickListener mListener;

    public SPCouponListAdapter(Context context, int type, ItemClickListener listener) {
        this.mContext = context;
        this.mType = type;
        this.mListener = listener;
    }

    public void updateData(List<SPCoupon> coupons) {
        if (coupons == null) return;
        this.mCoupons = coupons;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.person_coupon_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        final SPCoupon coupon = mCoupons.get(position);
        String money = coupon.getMoney();
        String condition = coupon.getCondition();
        String userTimeText = "";
        String moneyFmt;
        if (!SPStringUtils.isEmpty(money))
            moneyFmt = "¥" + money;
        else
            moneyFmt = "¥0";
        float fontSize = mContext.getResources().getDimension(R.dimen.textSizeMedium);
        SpannableString moneySpanStr = SPUtils.getFirstCharScale(moneyFmt, fontSize);
        holder.moneyTxtv.setText(moneySpanStr);
        holder.nameTxtv.setText("【满" + condition + "使用】");
        holder.limitCategoryTxtv.setText(coupon.getName());
        holder.limitPlatformTxtv.setText(coupon.getLimitStore());
        holder.codeTxtv.setText(coupon.getCode());
        switch (this.mType) {
            case 0:      //未使用
                if (!SPStringUtils.isEmpty(coupon.getUseEndTime()))
                    userTimeText = "有效期至:" + SPUtils.convertFullTimeFromPhpTime(Long.valueOf(coupon.getUseEndTime()), "yyyy.MM.dd");
                holder.couponTopRlayout.setBackgroundColor(mContext.getResources().getColor(R.color.coupon_blue));
                holder.useBtn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_coupon_blue));
                holder.useBtn.setTextColor(mContext.getResources().getColor(R.color.coupon_blue));
                break;
            case 1:      //已使用
                if (!SPStringUtils.isEmpty(coupon.getUseTime()))
                    userTimeText = "使用时间:" + SPUtils.convertFullTimeFromPhpTime(Long.valueOf(coupon.getUseTime()), "yyyy.MM.dd");
                holder.couponTopRlayout.setBackgroundColor(mContext.getResources().getColor(R.color.coupon_gray));
                holder.useBtn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_coupon_gray));
                holder.useBtn.setTextColor(mContext.getResources().getColor(R.color.coupon_gray));
                break;
            case 2:      //已过期
                if (!SPStringUtils.isEmpty(coupon.getUseTime()))
                    userTimeText = "过期时间:" + SPUtils.convertFullTimeFromPhpTime(Long.valueOf(coupon.getUseEndTime()), "yyyy.MM.dd");
                holder.couponTopRlayout.setBackgroundColor(mContext.getResources().getColor(R.color.coupon_gray));
                holder.useBtn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_coupon_gray));
                holder.useBtn.setTextColor(mContext.getResources().getColor(R.color.coupon_gray));
                break;
        }
        holder.useEndTimeTxtv.setText(userTimeText);
        holder.useBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onItemClick(mType, coupon);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCoupons == null) return 0;
        return mCoupons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View couponRlayout;
        View couponTopRlayout;
        TextView moneyTxtv;                     //抵扣金额
        TextView nameTxtv;                      //名称
        TextView useEndTimeTxtv;                //过期时间
        TextView limitCategoryTxtv;             //限品类
        TextView limitPlatformTxtv;             //限平台
        TextView codeTxtv;                      //编号
        Button useBtn;                          //立即使用

        public ViewHolder(View itemView) {
            super(itemView);
            couponRlayout = itemView.findViewById(R.id.coupon_rlayout);
            couponTopRlayout = itemView.findViewById(R.id.coupon_top_rlayout);
            nameTxtv = (TextView) itemView.findViewById(R.id.name_txtv);
            moneyTxtv = (TextView) itemView.findViewById(R.id.coupon_money_txtv);
            useEndTimeTxtv = (TextView) itemView.findViewById(R.id.use_end_time_txtv);
            limitCategoryTxtv = (TextView) itemView.findViewById(R.id.limit_category_val_txtv);
            limitPlatformTxtv = (TextView) itemView.findViewById(R.id.limit_platform_val_txtv);
            codeTxtv = (TextView) itemView.findViewById(R.id.code_val_txtv);
            useBtn = (Button) itemView.findViewById(R.id.use_btn);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int type, SPCoupon coupon);
    }

}
