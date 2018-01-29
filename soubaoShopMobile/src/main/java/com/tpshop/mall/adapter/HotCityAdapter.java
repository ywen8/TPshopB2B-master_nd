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
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.model.SPCity;

import java.util.List;

public class HotCityAdapter extends BaseAdapter {

    private Context mContext;
    private List<SPCity> cities;

    public HotCityAdapter(Context mContext, List<SPCity> cities) {
        this.mContext = mContext;
        this.cities = cities;
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
        SPCity city = cities.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.city_item, null);
            viewHolder.tvCity = (TextView) view.findViewById(R.id.tv_city);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvCity.setText(city.getName());
        return view;
    }

    class ViewHolder {
        TextView tvCity;
    }

}