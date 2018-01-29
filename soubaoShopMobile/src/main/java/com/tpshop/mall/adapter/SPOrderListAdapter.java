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
 * Description: 我的 -> 订单列表 -> 订单Adapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.OrderButtonModel;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.order.SPOrder;
import com.tpshop.mall.utils.SPOrderUtils;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPOrderListAdapter extends SectionedRecyclerViewAdapter<SPOrderListAdapter.HeaderViewHolder, SPOrderListAdapter.OrderItemViewHolder,
        SPOrderListAdapter.FooterViewHolder> implements View.OnClickListener {

    private Context mContext;
    private Handler mHandler;
    private List<SPOrder> mOrders;
    private LayoutInflater mInflater;

    public SPOrderListAdapter(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    public void updateData(List<SPOrder> orders) {
        if (orders == null)
            orders = new ArrayList<>();
        this.mOrders = orders;
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Message msg = null;
        if (v.getId() == R.id.store_name_txtv) {
            msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_STORE_HOME_ACTION);
            msg.obj = v.getTag();
        } else {
            int tag = Integer.valueOf(v.getTag(R.id.key_tag_index1).toString());
            int position = Integer.valueOf(v.getTag(R.id.key_tag_index2).toString());
            if (mHandler != null) {
                msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_ORDER_BUTTON_ACTION);
                msg.obj = mOrders.get(position);
                msg.what = tag;
            }
        }
        mHandler.sendMessage(msg);
    }

    @Override
    protected int getSectionCount() {
        if (mOrders == null) return 0;
        return mOrders.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (mOrders == null || mOrders.get(section).getProducts() == null) return 0;
        return mOrders.get(section).getProducts().size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.order_list_store_section_item, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    protected SPOrderListAdapter.FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.order_list_item_footer, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    protected SPOrderListAdapter.OrderItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.order_list_item, parent, false);
        return new SPOrderListAdapter.OrderItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        SPOrder order = mOrders.get(section);
        String name = (SPStringUtils.isEmpty(order.getStore().getStoreName())) ? "店铺名称异常" : order.getStore().getStoreName();
        holder.storeNameTxtv.setText(name);
        holder.storeNameTxtv.setTag(order.getStore().getStoreId());
        String status = SPStringUtils.isEmpty(order.getOrderStatusDesc()) ? "状态异常" : order.getOrderStatusDesc();
        holder.orderStatusTxtv.setText(status);
        holder.storeNameTxtv.setOnClickListener(this);
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder holder, int section) {
        SPOrder order = mOrders.get(section);
        String orderAmout = order.getOrderAmount();
        holder.orderButtonScrollv.setOnClickListener(orderButtonClickListener);
        holder.orderButtonScrollv.setTag(section);
        List<OrderButtonModel> buttonModels = getOrderButtonModelByOrder(order);
        buildProductButtonGallery(holder.orderButtonGalleryLyaout, buttonModels, section);
        int count = 0;
        if (order.getProducts() != null && order.getProducts().size() > 0)
            count = SPOrderUtils.getProductCount(order);
        String payInfo = "共" + count + "件商品 实付款:¥" + orderAmout + ("含运费(¥" + order.getShippingPrice() + ")");
        int startIndex = 10 + String.valueOf(count).length();
        int endIndex = startIndex + orderAmout.length();
        SpannableString payInfoSpanStr = new SpannableString(payInfo);
        payInfoSpanStr.setSpan(new RelativeSizeSpan(1.3f), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.orderProductDetailTxtv.setText(payInfoSpanStr);
    }

    @Override
    protected void onBindItemViewHolder(OrderItemViewHolder holder, int section, int position) {
        SPProduct product = mOrders.get(section).getProducts().get(position);
        if (mOrders.get(section).getProducts().size() > 0 && mOrders.get(section).getProducts().size() != (position + 1))
            holder.orderProductLine.setVisibility(View.VISIBLE);
        else
            holder.orderProductLine.setVisibility(View.GONE);
        holder.productNameTxtv.setText(product.getGoodsName());
        holder.productCountTxtv.setText("x" + product.getGoodsNum());
        holder.productPriceTxtv.setText("¥" + product.getMemberGoodsPrice());
        String specName = (SPStringUtils.isEmpty(product.getSpecKeyName())) ? "规格:无" : product.getSpecKeyName();
        holder.productSpecTxtv.setText(specName);
        holder.itemView.setOnClickListener(orderButtonClickListener);
        holder.itemView.setTag(section);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, product.getGoodsID());
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.productImgv);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView storeNameTxtv;
        ImageView storeLogoImgv;
        TextView orderStatusTxtv;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            storeLogoImgv = (ImageView) itemView.findViewById(R.id.store_logo_imgv);
            storeNameTxtv = (TextView) itemView.findViewById(R.id.store_name_txtv);
            orderStatusTxtv = (TextView) itemView.findViewById(R.id.order_status_txtv);
        }
    }

    //底部操作按钮View
    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView orderProductDetailTxtv;
        LinearLayout orderButtonGalleryLyaout;
        HorizontalScrollView orderButtonScrollv;

        public FooterViewHolder(View itemView) {
            super(itemView);
            orderButtonScrollv = (HorizontalScrollView) itemView.findViewById(R.id.order_button_scrollv);
            orderButtonGalleryLyaout = (LinearLayout) itemView.findViewById(R.id.order_button_gallery_lyaout);
            orderProductDetailTxtv = (TextView) itemView.findViewById(R.id.order_product_detail_txtv);
        }
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        View orderProductLine;
        ImageView productImgv;
        TextView productSpecTxtv;
        TextView productNameTxtv;
        TextView productPriceTxtv;
        TextView productCountTxtv;

        OrderItemViewHolder(View itemView) {
            super(itemView);
            productImgv = (ImageView) itemView.findViewById(R.id.product_pic_imgv);
            productNameTxtv = (TextView) itemView.findViewById(R.id.product_name_txtv);
            productPriceTxtv = (TextView) itemView.findViewById(R.id.product_price_txtv);
            productSpecTxtv = (TextView) itemView.findViewById(R.id.product_spec_txtv);
            productCountTxtv = (TextView) itemView.findViewById(R.id.product_count_txtv);
            orderProductLine = itemView.findViewById(R.id.order_product_line);
        }
    }

    /**
     * 根据 OrderButtonModel 动态创建对应的操作按钮
     */
    private void buildProductButtonGallery(LinearLayout gallery, List<OrderButtonModel> buttonModels, int position) {
        gallery.removeAllViews();
        if (buttonModels != null && buttonModels.size() > 0) {
            for (int i = 0; i < buttonModels.size(); i++) {
                OrderButtonModel buttonModel = buttonModels.get(i);
                View view = LayoutInflater.from(mContext).inflate(R.layout.order_button_gallery_item, gallery, false);
                Button button = (Button) view.findViewById(R.id.id_index_gallery_item_button);
                button.setText(buttonModel.getText());
                button.setFocusable(false);
                button.setTag(R.id.key_tag_index1, buttonModel.getTag());
                button.setTag(R.id.key_tag_index2, position);
                if (buttonModel.isLight()) {       //高亮显示
                    button.setBackgroundResource(R.drawable.button_border_r_selector);
                    button.setTextColor(mContext.getResources().getColor(R.color.light_red));
                } else {        //非高亮显示
                    button.setBackgroundResource(R.drawable.button_border_w_selector);
                    button.setTextColor(mContext.getResources().getColor(R.color.black));
                }
                button.setOnClickListener(this);
                gallery.addView(view);
            }
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.order_button_gallery_item, gallery, false);
            Button button = (Button) view.findViewById(R.id.id_index_gallery_item_button);
            button.setText("联系客服");
            button.setFocusable(false);
            button.setTag(R.id.key_tag_index1, SPMobileConstants.tagBuyAgain);
            button.setTag(R.id.key_tag_index2, position);
            button.setBackgroundResource(R.drawable.tag_button_bg_unchecked);
            button.setOnClickListener(this);
            gallery.addView(view);
        }
    }

    /**
     * 根据订单状态,获取需要显示的按钮model
     */
    private List<OrderButtonModel> getOrderButtonModelByOrder(SPOrder order) {
        List<OrderButtonModel> buttons = new ArrayList<>();
        if (order.getButtom().getPayBtn() == 1) {        //显示支付按钮
            OrderButtonModel payModel = new OrderButtonModel("支付", SPMobileConstants.tagPay, true);
            buttons.add(payModel);
        }
        if (order.getButtom().getCancelBtn() == 1) {        //取消订单按钮
            OrderButtonModel cancelModel = new OrderButtonModel("取消订单", SPMobileConstants.tagCancel, false);
            buttons.add(cancelModel);
        }
        if (order.getButtom().getReceiveBtn() == 1) {        //确认收货按钮
            OrderButtonModel receiveModel = new OrderButtonModel("确认收货", SPMobileConstants.tagReceive, true);
            buttons.add(receiveModel);
        }
        if (order.getButtom().getShippingBtn() == 1) {          //查看物流按钮
            OrderButtonModel shippingModel = new OrderButtonModel("查看物流", SPMobileConstants.tagShopping, true);
            buttons.add(shippingModel);
        }
        return buttons;
    }

    private View.OnClickListener orderButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = Integer.valueOf(v.getTag().toString());
            if (mHandler != null) {
                Message msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_ORDER_LIST_ITEM_ACTION);
                msg.obj = mOrders.get(position);
                mHandler.sendMessage(msg);
            }
        }
    };

}
