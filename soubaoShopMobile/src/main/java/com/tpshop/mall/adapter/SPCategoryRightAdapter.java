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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPCategoryRightAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter, View.OnClickListener {

    private Context mContext;
    private int totalCount = 0;
    private OnAdListener mListener;
    private List<SPCategory> mCategoryLst;

    public SPCategoryRightAdapter(Context context, OnAdListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        if (mCategoryLst == null) return 0;
        return totalCount;
    }

    public void setData(List<SPCategory> categorys) {
        if (categorys != null) {
            this.mCategoryLst = new ArrayList<>();
            for (SPCategory category : categorys) {
                List<SPCategory> subCategorys = category.getSubCategory();
                if (subCategorys != null) {
                    int size = subCategorys.size();
                    for (SPCategory subCategory : subCategorys) {
                        if (subCategory != null) {
                            subCategory.setParentCategory(category);
                            this.mCategoryLst.add(subCategory);
                        }
                    }
                    int sy = size % 3;       //每一行不足3个的填充3个,以下代码没有任何业务逻辑意义,只是为了适配页面显示效果
                    int count = (sy == 0) ? 0 : (3 - sy);
                    if (count >= 0) {
                        for (int i = 0; i < count; i++) {
                            SPCategory thirdCategory = new SPCategory();
                            thirdCategory.setParentCategory(category);
                            thirdCategory.setIsBlank(true);
                            mCategoryLst.add(thirdCategory);
                        }
                    }
                }
            }
            totalCount = this.mCategoryLst.size();
            this.notifyDataSetChanged();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mCategoryLst == null) return null;
        return mCategoryLst.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mCategoryLst == null) return -1;
        return mCategoryLst.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.category_right_item, parent, false);
            holder.r1View = convertView.findViewById(R.id.category_item_r1_layout);
            holder.r1Imgv = (ImageView) convertView.findViewById(R.id.category_item_r1_imgv);
            holder.r1Txtv = (TextView) convertView.findViewById(R.id.category_item_r1_txtv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mCategoryLst == null)
            return null;
        SPCategory category;
        if (position < mCategoryLst.size()) {
            category = mCategoryLst.get(position);
            if (category.isBlank()) {
                holder.r1Txtv.setVisibility(View.INVISIBLE);
                holder.r1Imgv.setVisibility(View.INVISIBLE);
            } else {
                holder.r1Txtv.setVisibility(View.VISIBLE);
                holder.r1Imgv.setVisibility(View.VISIBLE);
                holder.r1Txtv.setText(category.getName());
            }
            if (!SPStringUtils.isEmpty(category.getImage())) {
                String imgUrl = SPMobileConstants.BASE_HOST + "/" + category.getImage();
                Glide.with(mContext).load(imgUrl).asBitmap().fitCenter().placeholder(R.drawable.category_default)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.r1Imgv);
            } else {
                holder.r1Imgv.setImageResource(R.drawable.category_default);
            }
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        SPCategory parentCategory = mCategoryLst.get(position).getParentCategory();
        return parentCategory.getId();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder headerHolder;
        if (convertView == null) {
            headerHolder = new HeaderViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.category_grid_ad_title_item, parent, false);
            headerHolder.titleTxtv = (TextView) convertView.findViewById(R.id.catelogy_right_title_txtv);
            headerHolder.adImgv = (ImageView) convertView.findViewById(R.id.catelogy_right_title_ad_imgv);
            convertView.setTag(headerHolder);
        } else {
            headerHolder = (HeaderViewHolder) convertView.getTag();
        }
        if (position == 0)
            headerHolder.adImgv.setVisibility(View.VISIBLE);
        else
            headerHolder.adImgv.setVisibility(View.GONE);
        SPCategory parentCategory = mCategoryLst.get(position).getParentCategory();
        headerHolder.titleTxtv.setText(parentCategory.getName());
        headerHolder.titleTxtv.setTag(parentCategory.getId());
        headerHolder.adImgv.setOnClickListener(this);
        headerHolder.titleTxtv.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        String catId = null;
        if (v.getId() == R.id.catelogy_right_title_txtv)
            catId = v.getTag().toString();
        if (mListener != null) mListener.onAdClick(catId);
    }

    class HeaderViewHolder {
        TextView titleTxtv;
        ImageView adImgv;
    }

    class ViewHolder {
        View r1View;
        ImageView r1Imgv;
        TextView r1Txtv;
    }

    public interface OnAdListener {
        void onAdClick(String cat_id);
    }

}
