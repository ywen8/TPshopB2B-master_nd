package com.tpshop.mall.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.widget.SwitchButton;

public class MessageSettingsAdapter extends BaseAdapter {

    private boolean[] mIsOn;
    private String[] mItemTile;
    private LayoutInflater mInflater = null;

    public MessageSettingsAdapter(Context context, String[] itemTile, boolean[] isOn) {
        this.mInflater = LayoutInflater.from(context);
        this.mItemTile = itemTile;
        this.mIsOn = isOn;
    }

    @Override
    public int getCount() {
        return mItemTile.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.message_settings_list_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.mes_set_tv);
            holder.switchButton = (SwitchButton) convertView.findViewById(R.id.mes_set_tb);
            if (Build.VERSION.SDK_INT <= 19) {          //在android4.4及以下按钮文字不占空间,所以不做调整
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.switchButton.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.switchButton.setLayoutParams(params);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.switchButton.setChecked(mIsOn[position]);
        holder.title.setText(mItemTile[position]);
        holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    setButtonOnOff(position, 1);
                else
                    setButtonOnOff(position, 0);
            }
        });
        return convertView;
    }

    //1:on开,2:off关
    private void setButtonOnOff(int type, int OnOff) {
        SPUserRequest.setMessageSwitch(type, OnOff, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
            }
        });
    }

    class ViewHolder {
        public TextView title;
        SwitchButton switchButton;
    }

}
