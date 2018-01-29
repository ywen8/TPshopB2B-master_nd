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
import android.widget.TextView;

import com.tpshop.mall.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPSearchkeyListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mSearchkeys;

    public SPSearchkeyListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<String> searchkeys) {
        if (searchkeys == null)
            this.mSearchkeys = new ArrayList<>();
        else
            this.mSearchkeys = searchkeys;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mSearchkeys == null) return 0;
        return mSearchkeys.size();
    }

    @Override
    public Object getItem(int position) {
        return mSearchkeys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.common_search_key_item, parent, false);
            holder.keyTxtv = (TextView) convertView.findViewById(R.id.search_item_key_txtv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String key = mSearchkeys.get(position);
        holder.keyTxtv.setText(key);
        return convertView;
    }

    class ViewHolder {
        TextView keyTxtv;
    }

}
