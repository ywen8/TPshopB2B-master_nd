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
 * Description:  商品收藏列表 dapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.shop.SPCollect;

import java.util.List;

/**
 * @author 飞龙
 */
public class SPCollectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SPCollect> mCollectLst;
    private GoodsCollectListener mListener;

    public SPCollectListAdapter(Context context, GoodsCollectListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void setData(List<SPCollect> collectLst) {
        if (collectLst == null) return;
        this.mCollectLst = collectLst;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.person_collect_list_item, null);
        return new GoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GoodViewHolder goodViewHolder = (GoodViewHolder) holder;
        final SPCollect collect = mCollectLst.get(position);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, collect.getGoodsID());
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(goodViewHolder.picImgv);
        if (!SPStringUtils.isEmpty(collect.getGoodsName()))
            goodViewHolder.nameTxtv.setText(collect.getGoodsName());
        if (!SPStringUtils.isEmpty(collect.getShopPrice()))
            goodViewHolder.priceTxtv.setText(collect.getShopPrice());
        goodViewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onCancelCollect(collect);
            }
        });
        goodViewHolder.picImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onGoodDetail(collect);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCollectLst == null) return 0;
        return mCollectLst.size();
    }

    private class GoodViewHolder extends RecyclerView.ViewHolder {
        Button delBtn;                 //取消收藏
        ImageView picImgv;             //商品图片
        TextView nameTxtv;             //商品名称
        TextView priceTxtv;            //商品价格

        GoodViewHolder(View itemView) {
            super(itemView);
            picImgv = (ImageView) itemView.findViewById(R.id.product_pic_imgv);
            nameTxtv = (TextView) itemView.findViewById(R.id.product_name_txtv);
            priceTxtv = (TextView) itemView.findViewById(R.id.product_price_txtv);
            delBtn = (Button) itemView.findViewById(R.id.delete_btn);
        }
    }

    public interface GoodsCollectListener {

        void onCancelCollect(SPCollect collect);            //取消收藏

        void onGoodDetail(SPCollect collect);               //商品详情

    }

}
