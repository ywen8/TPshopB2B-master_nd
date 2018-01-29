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
 * Description: 店铺产品分类 adapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.model.SPStoreGoodsClass;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPStoreGoodsClassAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private Context mContext;
    GoodsClassListener mListener;
    private List<SPStoreGoodsClass> mGoodsClassLst;

    public SPStoreGoodsClassAdapter(Context context, GoodsClassListener Listener) {
        this.mContext = context;
        this.mListener = Listener;
    }

    public void setData(List<SPStoreGoodsClass> goodsClassLst) {
        if (goodsClassLst == null) return;
        this.mGoodsClassLst = new ArrayList<>();
        for (SPStoreGoodsClass gclass : goodsClassLst) {           //获取所有父分类的子分类并将父分类设置到子分类中
            if (gclass.getChildrenGoodsClasses() != null) {
                for (SPStoreGoodsClass subClass : gclass.getChildrenGoodsClasses()) {       //循环子分类
                    subClass.setParentGoodsClass(gclass);
                    this.mGoodsClassLst.add(subClass);
                }
            } else {        //父分类没有子分类,那么该分类只有一级
                SPStoreGoodsClass subClass = new SPStoreGoodsClass();
                subClass.setId(0);
                subClass.setParentGoodsClass(gclass);
                this.mGoodsClassLst.add(subClass);
            }
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mGoodsClassLst == null) return 0;
        return mGoodsClassLst.size();
    }

    @Override
    public Object getItem(int position) {
        if (mGoodsClassLst == null) return null;
        return mGoodsClassLst.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mGoodsClassLst == null) return -1;
        return mGoodsClassLst.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.store_goods_class_item, parent, false);
            holder.classLayout = (LinearLayout) convertView.findViewById(R.id.class_layout);
            holder.titleTxtv = (TextView) convertView.findViewById(R.id.title_txtv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SPStoreGoodsClass storeGoodsClass = mGoodsClassLst.get(position);
        if (storeGoodsClass.getId() == 0) {
            convertView.setLayoutParams(new GridView.LayoutParams(0, 0));
            return convertView;
        }
        holder.titleTxtv.setText(storeGoodsClass.getName());
        holder.titleTxtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onItemClick(storeGoodsClass);
            }
        });
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        SPStoreGoodsClass parentGoodsClass = mGoodsClassLst.get(position).getParentGoodsClass();
        return parentGoodsClass.getId();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder headerHolder;
        if (convertView == null) {
            headerHolder = new HeaderViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.store_goods_class_grid_title_item, parent, false);
            headerHolder.titleTxtv = (TextView) convertView.findViewById(R.id.store_goods_class_title_txtv);
            headerHolder.lookMoreTxtv = (TextView) convertView.findViewById(R.id.look_more_txtv);
            convertView.setTag(headerHolder);
        } else {
            headerHolder = (HeaderViewHolder) convertView.getTag();
        }
        final SPStoreGoodsClass gClass = mGoodsClassLst.get(position).getParentGoodsClass();
        headerHolder.titleTxtv.setText(gClass.getName());
        headerHolder.lookMoreTxtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onClassItemClick(gClass);
            }
        });
        return convertView;
    }

    class ViewHolder {
        LinearLayout classLayout;
        TextView titleTxtv;
    }

    class HeaderViewHolder {
        TextView titleTxtv;
        TextView lookMoreTxtv;
    }

    public interface GoodsClassListener {

        void onClassItemClick(SPStoreGoodsClass goodsClass);

        void onItemClick(SPStoreGoodsClass goodsClass);

    }

}
