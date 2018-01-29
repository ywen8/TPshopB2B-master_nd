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
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.shop.SPProductDetailActivity_;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPCommentData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuhao
 */
public class SPOrderCenterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mType;
    private Context mContext;
    private List<SPCommentData> mComment;
    private OnItemClickListener mListener;

    public SPOrderCenterListAdapter(Context context, OnItemClickListener listener, int type) {
        this.mContext = context;
        this.mListener = listener;
        this.mType = type;
    }

    public void updateData(List<SPCommentData> comment) {
        if (comment != null)
            this.mComment = comment;
        else
            this.mComment = new ArrayList<>();
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.order_center_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (mType == 0) {
            if (mComment.get(position).getGoodsId() != null) {
                String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, mComment.get(position).getGoodsId());
                Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.goodsPicImgv);
            }
            holder.productNameTxtv.setText(mComment.get(position).getGoodsName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        gotoProductDetail(mComment.get(position).getGoodsId(), mContext);
                }
            });
            holder.imgBtn.setImageResource(R.drawable.order_center_message);
            holder.imgBtnBorder.setBackgroundResource(R.drawable.button_border_r_selector);
            holder.imgBtnBorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onItemClick(mComment.get(position));
                }
            });
        } else if (mType == 1) {
            String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, mComment.get(position).getGoodsId());
            Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.goodsPicImgv);
            holder.productNameTxtv.setText(mComment.get(position).getGoodsName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoProductDetail(mComment.get(position).getGoodsId(), mContext);
                }
            });
            holder.imgBtn.setImageResource(R.drawable.order_center_look);
            holder.useBtn.setTextColor(mContext.getResources().getColor(R.color.button_bg_gray));
            holder.useBtn.setText("查看评价");
            holder.imgBtnBorder.setBackgroundResource(R.drawable.button_border_w_selector);
            holder.imgBtnBorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data = new Intent(mContext, SPProductDetailActivity_.class);
                    data.putExtra("goodsID", mComment.get(position).getGoodsId());
                    data.putExtra("ShowOrderItem", true);
                    mContext.startActivity(data);
                }
            });

        }
    }

    private void gotoProductDetail(String googId, Context context) {
        Intent intent = new Intent(context, SPProductDetailActivity_.class);
        intent.putExtra("goodsID", googId);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mComment == null) return 0;
        return mComment.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView useBtn;                 //晒单评价按钮文字
        ImageView imgBtn;                //晒单按钮图片
        TextView conditionTxtv;          //说明
        ImageView goodsPicImgv;          //商品图片
        TextView productNameTxtv;        //商品名字
        RelativeLayout imgBtnBorder;     //晒单按钮边框

        public ViewHolder(View itemView) {
            super(itemView);
            goodsPicImgv = (ImageView) itemView.findViewById(R.id.product_pic_imgv);
            productNameTxtv = (TextView) itemView.findViewById(R.id.product_name_txtv);
            conditionTxtv = (TextView) itemView.findViewById(R.id.condition_txtv);
            useBtn = (TextView) itemView.findViewById(R.id.order_show_btn);
            imgBtn = (ImageView) itemView.findViewById(R.id.btn_img);
            imgBtnBorder = (RelativeLayout) itemView.findViewById(R.id.btn_img_rl);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SPCommentData comment);
    }

}
