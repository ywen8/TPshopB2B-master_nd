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
 * Description: 消息推送 -> 消息推送 adapter
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
import com.tpshop.mall.model.SPPushMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPPushMessageListAdapter extends BaseAdapter {

    private Context mContext;
    private List<SPPushMessage> mMessageLst;

    public SPPushMessageListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<SPPushMessage> messageLst) {
        if (messageLst == null)
            this.mMessageLst = new ArrayList<>();
        else
            this.mMessageLst = messageLst;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mMessageLst == null) return 0;
        return mMessageLst.size();
    }

    @Override
    public Object getItem(int position) {
        if (mMessageLst == null) return null;
        return mMessageLst.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mMessageLst == null) return -1;
        return mMessageLst.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.push_message_list_item, parent, false);
            holder.titleTxtv = (TextView) convertView.findViewById(R.id.title_txtv);
            holder.timeTxtv = (TextView) convertView.findViewById(R.id.time_txtv);
            holder.messageTxtv = (TextView) convertView.findViewById(R.id.message_txtv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SPPushMessage pushMessage = mMessageLst.get(position);
        if (pushMessage.getIsRead() == 1) {         //是否已读
            holder.titleTxtv.setTextAppearance(mContext, R.style.textStyle_Medium_subTitle);
            holder.timeTxtv.setTextAppearance(mContext, R.style.textStyle_Normal_subTitle);
            holder.messageTxtv.setTextAppearance(mContext, R.style.textStyle_Normal_subTitle);
        } else {
            holder.titleTxtv.setTextAppearance(mContext, R.style.textStyle_Medium_black);
            holder.timeTxtv.setTextAppearance(mContext, R.style.textStyle_Normal_black);
            holder.messageTxtv.setTextAppearance(mContext, R.style.textStyle_Normal_black);
        }
        holder.titleTxtv.setText(pushMessage.getTitle());
        holder.timeTxtv.setText(pushMessage.getReceiverTime());
        holder.messageTxtv.setText(pushMessage.getMessage());
        return convertView;
    }

    class ViewHolder {
        TextView timeTxtv;
        TextView titleTxtv;
        TextView messageTxtv;
    }

}
