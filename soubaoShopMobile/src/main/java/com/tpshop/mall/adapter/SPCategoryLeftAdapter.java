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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.model.SPCategory;

import java.util.List;

/**
 * @author 飞龙
 */
public class SPCategoryLeftAdapter extends BaseAdapter {

    private Context mContext;
    private int curSelectRow = -1;              //当前被选中的行索引
    private List<SPCategory> mCategoryLst;

    public SPCategoryLeftAdapter(Context context) {
        this.mContext = context;
    }

    public void setSelectIndex(int selectIndex) {
        curSelectRow = selectIndex;
    }

    public void setData(List<SPCategory> categorys) {
        if (categorys == null) return;
        this.mCategoryLst = categorys;
    }

    @Override
    public int getCount() {
        if (mCategoryLst == null) return 0;
        return mCategoryLst.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.category_left_item, parent, false);
            holder.nameTxtv = (TextView) convertView.findViewById(R.id.catelogy_left_item_name);
            holder.highLinev = convertView.findViewById(R.id.high_linev);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (curSelectRow == position) {         //选中的行:背景白色,否则灰色
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.bg_activity));
            holder.nameTxtv.setTextColor(mContext.getResources().getColor(R.color.light_red));
            holder.highLinev.setVisibility(View.VISIBLE);
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.bg_view));
            holder.nameTxtv.setTextColor(Color.parseColor("#222222"));
            holder.highLinev.setVisibility(View.INVISIBLE);
        }
        SPCategory category = mCategoryLst.get(position);
        holder.nameTxtv.setText(category.getName());
        return convertView;
    }

    class ViewHolder {
        TextView nameTxtv;
        View highLinev;
    }

}
