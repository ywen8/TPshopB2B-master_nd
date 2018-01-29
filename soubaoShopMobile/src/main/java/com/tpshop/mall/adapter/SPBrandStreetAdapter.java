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
 * Description: 分类 -> 左边菜单 adapter
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.shop.SPBrand;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPBrandStreetAdapter extends RecyclerView.Adapter<SPBrandStreetAdapter.BrandViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<SPBrand> mBrandList;
    private OnBrandItemClickListener mListener;

    public SPBrandStreetAdapter(Context context, OnBrandItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void updateData(List<SPBrand> brands) {
        if (brands != null)
            mBrandList = brands;
        else
            mBrandList = new ArrayList<>();
        this.notifyDataSetChanged();
    }

    @Override
    public SPBrandStreetAdapter.BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.brand_street_item, parent, false);
        return new SPBrandStreetAdapter.BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SPBrandStreetAdapter.BrandViewHolder holder, int position) {
        SPBrand brand = mBrandList.get(position);
        String url = SPMobileConstants.BASE_HOST + brand.getLogo();
        Glide.with(mContext).load(url).asBitmap().fitCenter().placeholder(R.drawable.icon_brand_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.brandLogoImgv);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(brand.getBrandId());
    }

    @Override
    public int getItemCount() {
        if (mBrandList != null) return mBrandList.size();
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) return;
        mListener.onItemClick(v.getTag().toString());
    }

    class BrandViewHolder extends RecyclerView.ViewHolder {
        ImageView brandLogoImgv;

        BrandViewHolder(View itemView) {
            super(itemView);
            brandLogoImgv = (ImageView) itemView.findViewById(R.id.brand_imgv);
        }
    }

    public interface OnBrandItemClickListener {
        void onItemClick(String brandId);
    }

}
