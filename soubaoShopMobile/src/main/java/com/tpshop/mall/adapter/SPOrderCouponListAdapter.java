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
public class SPOrderCouponListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SPCoupon> mCoupons;
    private ItemClickListener mListener;

    public SPOrderCouponListAdapter(Context context, ItemClickListener listener) {
        this.mContext = context;
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
        View view = mInflater.inflate(R.layout.order_coupon_list_item, parent, false);
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
        float fontSize = mContext.getResources().getDimension(R.dimen.textSizeNormal);
        SpannableString moneySpanStr = SPUtils.getFirstCharScale(moneyFmt, fontSize);
        holder.moneyTxtv.setText(moneySpanStr);
        holder.conditionTv.setText("【满" + condition + "使用】");
        holder.couponLimit.setText("【" + coupon.getLimitStore() + "】");
        holder.couponName.setText(coupon.getName());
        if (!SPStringUtils.isEmpty(coupon.getUseEndTime()))
            userTimeText = SPUtils.convertFullTimeFromPhpTime(Long.valueOf(coupon.getUseStartTime()), "yyyy.MM.dd") + "——"
                    + SPUtils.convertFullTimeFromPhpTime(Long.valueOf(coupon.getUseEndTime()), "yyyy.MM.dd");
        holder.useEndTimeTxtv.setText(userTimeText);
        if (coupon.isCheck())
            holder.checkBtn.setBackgroundResource(R.drawable.icon_checked);
        else
            holder.checkBtn.setBackgroundResource(R.drawable.icon_checkno);
        holder.checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (coupon.isCheck())
                        mListener.onItemClick(coupon, false);
                    else
                        mListener.onItemClick(coupon, true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCoupons == null) return 0;
        return mCoupons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button checkBtn;                      //选择按钮
        View couponLayout;
        TextView moneyTxtv;                   //抵扣金额
        TextView couponName;                  //名称
        TextView conditionTv;                 //使用条件
        TextView couponTitle;                 //优惠券标题
        TextView couponLimit;                 //店铺限制
        TextView useEndTimeTxtv;              //过期时间

        public ViewHolder(View itemView) {
            super(itemView);
            couponLayout = itemView.findViewById(R.id.coupon_layout);
            moneyTxtv = (TextView) itemView.findViewById(R.id.coupon_money_txtv);
            conditionTv = (TextView) itemView.findViewById(R.id.condition_txtv);
            couponTitle = (TextView) itemView.findViewById(R.id.coupon_title_txtv);
            couponLimit = (TextView) itemView.findViewById(R.id.coupon_limit_txtv);
            couponName = (TextView) itemView.findViewById(R.id.coupon_name_txtv);
            useEndTimeTxtv = (TextView) itemView.findViewById(R.id.use_end_time_txtv);
            checkBtn = (Button) itemView.findViewById(R.id.check_btn);
        }
    }

    public interface ItemClickListener {
        void onItemClick(SPCoupon coupon, boolean checked);
    }

}
