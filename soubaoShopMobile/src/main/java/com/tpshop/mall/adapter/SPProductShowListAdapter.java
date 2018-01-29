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
 * Description:  确认订单, 订单列表, 订单详情 -> 商品列表展示 dapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.utils.SPOrderUtils;

import java.util.List;

/**
 * @author 飞龙
 */
public class SPProductShowListAdapter extends BaseAdapter {

    private Context mContext;
    private Handler mHandler;
    private List<SPProduct> mProductLst;

    public SPProductShowListAdapter(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    public void setData(List<SPProduct> products) {
        if (products == null) return;
        this.mProductLst = products;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mProductLst == null) return 0;
        return mProductLst.size();
    }

    @Override
    public Object getItem(int position) {
        if (mProductLst == null) return null;
        return mProductLst.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mProductLst == null) return -1;
        return Long.valueOf(mProductLst.get(position).getGoodsID());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.product_show_list_item, parent, false);
            holder.picIngv = (ImageView) convertView.findViewById(R.id.product_pic_imgv);
            holder.nameTxtv = (TextView) convertView.findViewById(R.id.product_name_txtv);
            holder.specTxtv = (TextView) convertView.findViewById(R.id.product_spec_txtv);
            holder.countTxtv = (TextView) convertView.findViewById(R.id.product_count_txtv);
            holder.priceTxtv = (TextView) convertView.findViewById(R.id.product_price_txtv);
            holder.applyReturnBtn = (Button) convertView.findViewById(R.id.product_apply_return_btn);
            holder.commentBtn = (Button) convertView.findViewById(R.id.product_apply_comment_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SPProduct product = mProductLst.get(position);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, product.getGoodsID());
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picIngv);
        if (!SPStringUtils.isEmpty(product.getGoodsName()))
            holder.nameTxtv.setText(product.getGoodsName());
        if (!SPStringUtils.isEmpty(product.getSpecKeyName()))
            holder.specTxtv.setText(product.getSpecKeyName());
        holder.countTxtv.setText("x" + product.getGoodsNum());
        if (!SPStringUtils.isEmpty(product.getGoodsPrice()))
            holder.priceTxtv.setText("¥" + product.getGoodsPrice());
        if (product.getReturnBtn() == 1 && product.getIsSend() == 1) {
            holder.applyReturnBtn.setVisibility(View.VISIBLE);
            holder.applyReturnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHandler != null) {
                        Message msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_AFTERSALE);
                        msg.obj = product;
                        mHandler.sendMessage(msg);
                    }
                }
            });
        } else {
            holder.applyReturnBtn.setVisibility(View.INVISIBLE);
        }
        if (product.getIsComment() == 0 && product.getOrderStatusCode().equals(SPOrderUtils.typeWaitComment)) {      //订单状态处于待评价
            holder.commentBtn.setVisibility(View.VISIBLE);
            holder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHandler != null) {
                        Message msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_COMMENT);
                        msg.obj = product;
                        mHandler.sendMessage(msg);
                    }
                }
            });
        } else {
            holder.commentBtn.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView picIngv;
        TextView nameTxtv;           //商品名称
        TextView specTxtv;           //商品规格
        Button commentBtn;           //评价
        TextView countTxtv;          //商品数量
        TextView priceTxtv;          //商品价格
        Button applyReturnBtn;       //申请售后
    }

}
