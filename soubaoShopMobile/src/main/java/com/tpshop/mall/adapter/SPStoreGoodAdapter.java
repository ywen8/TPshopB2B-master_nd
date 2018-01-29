package com.tpshop.mall.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.tpshop.mall.activity.shop.SPProductDetailActivity_;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.distribute.SPStoreGood;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2017/6/9
 */
public class SPStoreGoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SPStoreGood> storeGoods;

    public SPStoreGoodAdapter(Context context, List<SPStoreGood> storeGoods) {
        this.mContext = context;
        this.storeGoods = storeGoods;
    }

    public void updateData(List<SPStoreGood> storeGoods) {
        if (storeGoods == null)
            storeGoods = new ArrayList<>();
        this.storeGoods = storeGoods;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.distribute_store_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final SPStoreGood storeGood = storeGoods.get(position);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, 400, 400, storeGood.getGoodsId() + "");
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.goodImg);
        viewHolder.goodName.setText(storeGood.getGoodsName());
        viewHolder.goodPrice.setText("￥" + storeGood.getGoodsPrice());
        viewHolder.goodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SPProductDetailActivity_.class);
                intent.putExtra("goodsID", storeGood.getGoodsId() + "");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.storeGoods == null) return 0;
        return this.storeGoods.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goodImg;
        TextView goodName;
        TextView goodPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            goodImg = (ImageView) itemView.findViewById(R.id.good_pic);
            goodName = (TextView) itemView.findViewById(R.id.good_name);
            goodPrice = (TextView) itemView.findViewById(R.id.good_price);
        }
    }

}
