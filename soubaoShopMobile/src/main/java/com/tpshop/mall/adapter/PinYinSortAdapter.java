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
 * Description: 首页 -> 定位 -> 区域列表
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.model.SPCity;

import java.util.List;

public class PinYinSortAdapter extends BaseAdapter implements SectionIndexer {

    private Context mContext;
    private List<SPCity> cities;
    private OnclickAddressListener mListener;

    public PinYinSortAdapter(Context mContext, OnclickAddressListener listener, List<SPCity> cities) {
        this.mContext = mContext;
        this.mListener = listener;
        this.cities = cities;
    }

    public void updateListView(List<SPCity> cities) {
        if (cities == null) return;
        this.cities = cities;
        notifyDataSetChanged();
    }

    public int getCount() {
        if (this.cities == null) return 0;
        return this.cities.size();
    }

    public Object getItem(int position) {
        if (this.cities == null) return null;
        return cities.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder;
        final SPCity city = cities.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.pinyin_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        int section = getSectionForPosition(position);             //根据position获取分类的首字母的Char ascii值
        if (position == getPositionForSection(section)) {          //如果当前位置等于该分类首字母的Char的位置,则认为是第一次出现
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(city.getInitial());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(city.getName());
        viewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.checkAddress(city);
            }
        });
        return view;
    }

    class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return cities.get(position).getInitial().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = cities.get(i).getInitial();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) return i;
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    public interface OnclickAddressListener {
        void checkAddress(SPCity city);
    }

}