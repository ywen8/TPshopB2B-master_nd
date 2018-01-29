package com.tpshop.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.model.order.SPInvoce;

import java.util.List;

/**
 * Created by zw on 2017/5/26
 */
public class OrderInvoceAdapter extends BaseAdapter {

    private ItemOnclick itemOnclick;
    private List<SPInvoce> spInvoces;
    private LayoutInflater layoutInflater;

    public OrderInvoceAdapter(Context context, List<SPInvoce> spInvoces, ItemOnclick itemOnclick) {
        this.layoutInflater = LayoutInflater.from(context);
        this.spInvoces = spInvoces;
        this.itemOnclick = itemOnclick;
    }

    @Override
    public int getCount() {
        return spInvoces.size();
    }

    @Override
    public Object getItem(int position) {
        return spInvoces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.order_invoce_item, null);
            holder.invoceImg = (ImageView) convertView.findViewById(R.id.invoce_img);
            holder.invoceTxt = (TextView) convertView.findViewById(R.id.invoce_txt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SPInvoce invoce = spInvoces.get(position);
        if (invoce.isCheck()) {
            holder.invoceImg.setImageResource(R.drawable.radio_checked);
        } else {
            holder.invoceImg.setImageResource(R.drawable.radio_nocheck);
        }
        holder.invoceTxt.setText(invoce.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnclick != null)
                    itemOnclick.click(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView invoceImg;
        TextView invoceTxt;
    }

    public interface ItemOnclick {
        void click(int position);
    }

}
