package com.tpshop.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.distribute.SPDistributeGood;
import com.tpshop.mall.model.distribute.SPDistributeOrder;
import com.tpshop.mall.utils.SPUtils;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * Created by zw on 2017/6/7
 */
public class SPDistributeOrderAdapter extends SectionedRecyclerViewAdapter<SPDistributeOrderAdapter.HeaderViewHolder,
        SPDistributeOrderAdapter.OrderItemViewHolder, SPDistributeOrderAdapter.FooterViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SPDistributeOrder> distributeOrders;

    public SPDistributeOrderAdapter(Context ctx, List<SPDistributeOrder> distributeOrders) {
        this.mContext = ctx;
        this.distributeOrders = distributeOrders;
    }

    public void updateBrowData(List<SPDistributeOrder> distributeOrders) {
        if (distributeOrders == null) return;
        this.distributeOrders = distributeOrders;
        this.notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        if (distributeOrders != null) return distributeOrders.size();
        return 0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return 1;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.distribution_order_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    protected FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.distribution_order_footer, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    protected OrderItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.distribution_order_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        SPDistributeOrder distributeOrder = distributeOrders.get(section);
        holder.buyUser.setText(distributeOrder.getNickName());
        String status = "";
        switch (distributeOrder.getStatus()) {
            case 0:
                status = "未付款";
                break;
            case 1:
                status = "已付款";
                break;
            case 2:
                status = "已收货";
                break;
            case 3:
                status = "已分成";
                break;
            case 4:
                status = "已取消";
                break;
        }
        holder.orderStatus.setText(status);                //0未付款,1已付款,2等待分成(已收货),3已分成,4已取消
        holder.orderSn.setText(distributeOrder.getOrderSn());
        holder.orderTime.setText(SPUtils.getFullTimeFormPhpTime(distributeOrder.getCreateTime()));
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder holder, int section) {
        SPDistributeOrder distributeOrder = distributeOrders.get(section);
        holder.distributeMoney.setText("￥" + distributeOrder.getMoney());
    }

    @Override
    protected void onBindItemViewHolder(OrderItemViewHolder holder, int section, int position) {
        final SPDistributeOrder distributeOrder = distributeOrders.get(section);
        List<SPDistributeGood> distributeGoods = distributeOrder.getDistributeGoods();
        if (distributeGoods != null && distributeGoods.size() > 1) {
            holder.orderRl1.setVisibility(View.GONE);
            holder.orderRl2.setVisibility(View.VISIBLE);
            holder.goodNum.setText("共" + distributeGoods.size() + "件商品");
            holder.goodimgLl.removeAllViews();
            for (SPDistributeGood distributeGood : distributeGoods) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SPCommonUtils.dip2px(mContext, 70),
                        SPCommonUtils.dip2px(mContext, 70));
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                String imgUrl = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, 400, 400, distributeGood.getGoodsId() + "");
                Glide.with(mContext).load(imgUrl).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                holder.goodimgLl.addView(imageView);
            }
        } else {
            holder.orderRl1.setVisibility(View.VISIBLE);
            holder.orderRl2.setVisibility(View.GONE);
            if (distributeGoods != null && distributeGoods.size() > 0) {
                final SPDistributeGood distributeGood = distributeGoods.get(0);
                String imgUrl = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, 400, 400, distributeGood.getGoodsId() + "");
                Glide.with(mContext).load(imgUrl).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.goodImg);
                holder.goodName.setText(distributeGood.getGoodsName());
                holder.goodPrice.setText("￥" + distributeGood.getGoodsPrice());
                String string;
                if (distributeGood.getGoodsPrice() != null) {
                    double rate;
                    rate = distributeGood.getCommission() / Double.parseDouble(distributeGood.getGoodsPrice()) * 100;
                    string = String.format("%.2f", rate);
                } else {
                    string = "0";
                }
                holder.goodRate.setText(string + "%");
                holder.goodMoney.setText("￥" + distributeGood.getCommission());
                holder.goodSpec.setText(distributeGood.getSpecName());
            } else {
                holder.orderRl1.setVisibility(View.GONE);
            }
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView buyUser;
        TextView orderSn;
        TextView orderTime;
        TextView orderStatus;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            buyUser = (TextView) itemView.findViewById(R.id.buy_user_name);
            orderStatus = (TextView) itemView.findViewById(R.id.order_status_txt);
            orderSn = (TextView) itemView.findViewById(R.id.order_sn_txt);
            orderTime = (TextView) itemView.findViewById(R.id.order_time_txt);
        }
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView goodNum;
        ImageView goodImg;
        TextView goodName;
        TextView goodRate;
        TextView goodSpec;
        TextView goodPrice;
        TextView goodMoney;
        LinearLayout goodimgLl;
        RelativeLayout orderRl1;
        RelativeLayout orderRl2;

        OrderItemViewHolder(View itemView) {
            super(itemView);
            orderRl1 = (RelativeLayout) itemView.findViewById(R.id.distribute_order_rl1);
            goodImg = (ImageView) itemView.findViewById(R.id.distribute_order_img);
            goodName = (TextView) itemView.findViewById(R.id.distribute_order_name);
            goodPrice = (TextView) itemView.findViewById(R.id.distribute_order_price);
            goodRate = (TextView) itemView.findViewById(R.id.distribute_money_rate);
            goodMoney = (TextView) itemView.findViewById(R.id.distribute_order_money);
            goodSpec = (TextView) itemView.findViewById(R.id.distribute_spec_txt);
            orderRl2 = (RelativeLayout) itemView.findViewById(R.id.distribute_order_rl2);
            goodimgLl = (LinearLayout) itemView.findViewById(R.id.distribute_goodimg_ll);
            goodNum = (TextView) itemView.findViewById(R.id.distribute_good_num);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView distributeMoney;

        public FooterViewHolder(View itemView) {
            super(itemView);
            distributeMoney = (TextView) itemView.findViewById(R.id.distribute_money_txt);
        }
    }

}
