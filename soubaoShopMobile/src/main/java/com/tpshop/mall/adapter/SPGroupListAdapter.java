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
import android.os.Handler;
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
import com.tpshop.mall.activity.shop.SPGroupListActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.shop.SPGroup;
import com.tpshop.mall.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPGroupListAdapter extends RecyclerView.Adapter<SPGroupListAdapter.ViewHolder> {

    private int mTicker = 0;
    private Context mContext;
    private List<SPGroup> mGroups;
    private List<TextView> endTxtvs;
    private EndTimeRunnable mRunnable;
    private OnItemClickListener mListener;

    public SPGroupListAdapter(SPGroupListActivity activity) {
        this.mContext = activity;
        this.mListener = activity;
        endTxtvs = new ArrayList<>();
    }

    public void updateData(List<SPGroup> groups) {
        if (groups == null) return;
        this.mGroups = groups;
        setTimeDown();
    }

    private void setTimeDown() {
        if (mGroups == null || endTxtvs == null) return;
        int count = mGroups.size();
        endTxtvs.clear();
        for (int i = 0; i < count; i++) {
            SPGroup group = mGroups.get(i);
            TextView endTimeTxtv = new TextView(mContext);
            String groupTip;
            Long[] times = SPUtils.getTimeCut(group.getServerTime() + mTicker, group.getEndTime());
            if (times.length == 4 && times[0] == 0 && times[1] == 0 && times[2] == 0 && times[3] == 0)
                groupTip = "已经结束";
            else
                groupTip = times[0] + "天" + times[1] + "时" + times[2] + "分" + times[3] + "秒";
            if (endTimeTxtv != null) endTimeTxtv.setText(groupTip);
            endTxtvs.add(endTimeTxtv);
        }
        this.notifyDataSetChanged();
        if (mRunnable != null) mRunnable.stop();
        mRunnable = new EndTimeRunnable();
        new Handler().postDelayed(mRunnable, 1000);
    }

    @Override
    public SPGroupListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.group_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SPGroupListAdapter.ViewHolder holder, int position) {
        final SPGroup group = mGroups.get(position);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, String.valueOf(group.getGoodsId()));
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.goodsPicImgv);
        holder.productNameTxtv.setText(group.getTitle());
        holder.rebateTxtv.setText(group.getRebate() + "折");
        holder.goodsPriceTxtv.setText("¥" + group.getPrice() + "元");                      //团购价
        holder.marketPriceTxtv.setText("¥" + group.getMarketPrice() + "元");               //市场价
        holder.virtualNumTxtv.setText((group.getVirtualNum() + group.getBuyNum()) + "人参与");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onItemClick(group.getGoodsId(), group.getItemId());
            }
        });
        holder.endTimeTxtv.setText(endTxtvs.get(position).getText());
    }

    @Override
    public int getItemCount() {
        if (mGroups == null) return 0;
        return mGroups.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsPicImgv;
        TextView productNameTxtv;
        TextView rebateTxtv;
        TextView goodsPriceTxtv;               //原价
        TextView marketPriceTxtv;              //现价
        TextView virtualNumTxtv;               //已售
        TextView endTimeTxtv;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsPicImgv = (ImageView) itemView.findViewById(R.id.product_pic_imgv);
            productNameTxtv = (TextView) itemView.findViewById(R.id.product_name_txtv);
            rebateTxtv = (TextView) itemView.findViewById(R.id.rebate_txtv);
            goodsPriceTxtv = (TextView) itemView.findViewById(R.id.goods_price_txtv);
            marketPriceTxtv = (TextView) itemView.findViewById(R.id.market_price_txtv);           //原价
            marketPriceTxtv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);                     //设置删除线
            virtualNumTxtv = (TextView) itemView.findViewById(R.id.virtual_num_txtv);
            endTimeTxtv = (TextView) itemView.findViewById(R.id.end_time_txtv);
        }
    }

    private class EndTimeRunnable implements Runnable {

        private boolean isStop = false;

        private void stop() {
            isStop = true;
        }

        @Override
        public void run() {
            if (isStop) return;
            mTicker++;
            setTimeDown();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String goodsId, String itemId);
    }

}
