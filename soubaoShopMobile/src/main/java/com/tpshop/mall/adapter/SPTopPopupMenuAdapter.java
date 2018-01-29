package com.tpshop.mall.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpshop.mall.R;

import java.util.ArrayList;

/**
 * Created by admin on 2016/8/17
 */
public class SPTopPopupMenuAdapter extends BaseAdapter {

    private int myType;
    private LayoutInflater inflater;
    private ArrayList<String> myItems;

    public SPTopPopupMenuAdapter(Context context, ArrayList<String> items, int type) {
        inflater = LayoutInflater.from(context);
        this.myItems = items;
        this.myType = type;
    }

    @Override
    public int getCount() {
        return myItems.size();
    }

    @Override
    public String getItem(int position) {
        return myItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopupHolder holder;
        if (convertView == null) {
            holder = new PopupHolder();
            convertView = inflater.inflate(R.layout.top_popup_item, null);
            holder.itemNameTv = (TextView) convertView.findViewById(R.id.popup_tv);
            if (myType == 0) {
                holder.itemNameTv.setGravity(Gravity.CENTER);
            } else if (myType == 1) {
                holder.itemNameTv.setGravity(Gravity.LEFT);
            } else if (myType == 2) {
                holder.itemNameTv.setGravity(Gravity.CENTER);
            }
            convertView.setTag(holder);
        } else {
            holder = (PopupHolder) convertView.getTag();
        }
        String itemName = getItem(position);
        holder.itemNameTv.setText(itemName);
        return convertView;
    }

    private class PopupHolder {
        TextView itemNameTv;
    }

}
