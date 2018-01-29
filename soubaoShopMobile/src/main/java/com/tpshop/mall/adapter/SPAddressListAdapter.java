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
 * Description: 我的 -> 收货人列表 -> 收货人列表 adapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.person.address.SPConsigneeAddressListActivity;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.utils.SPConfirmDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPAddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SPConfirmDialog.ConfirmDialogListener {

    private Context mContext;
    private SPConsigneeAddress selectConsignee;
    private List<SPConsigneeAddress> mConsignees;
    private AddressListListener mAddressListListener;

    public SPAddressListAdapter(Context context, AddressListListener addressListListener) {
        this.mContext = context;
        this.mAddressListListener = addressListListener;
    }

    public void updateData(List<SPConsigneeAddress> consignees) {
        if (consignees == null)
            consignees = new ArrayList<>();
        this.mConsignees = consignees;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mConsignees == null) return 0;
        return mConsignees.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.person_address_list_item_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        final SPConsigneeAddress consignee = mConsignees.get(position);
        holder.consigneeTxtv.setText(consignee.getConsignee());
        holder.mobileTxtv.setText(consignee.getMobile());
        holder.addressTxtv.setText(consignee.getFullAddress());
        if ("1".equals(consignee.getIsDefault()))
            holder.setDefaultTxtv.setVisibility(View.VISIBLE);
        else
            holder.setDefaultTxtv.setVisibility(View.GONE);
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddressListListener != null) mAddressListListener.onItemEdit(consignee);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddressListListener != null) mAddressListListener.onItemClick(consignee);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectConsignee = consignee;
                ((SPConsigneeAddressListActivity) mContext).showConfirmDialog("确定删除该地址吗", "删除提醒", SPAddressListAdapter.this, 1);
                return true;
            }
        });
    }

    @Override
    public void clickOk(int actionType) {
        if (mAddressListListener != null) mAddressListListener.onItemDel(selectConsignee);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView consigneeTxtv;
        TextView mobileTxtv;
        TextView addressTxtv;
        ImageView editBtn;
        TextView setDefaultTxtv;

        public ViewHolder(View itemView) {
            super(itemView);
            consigneeTxtv = (TextView) itemView.findViewById(R.id.address_consignee_txtv);
            mobileTxtv = (TextView) itemView.findViewById(R.id.address_mobile_txtv);
            addressTxtv = (TextView) itemView.findViewById(R.id.address_detail_txtv);
            editBtn = (ImageView) itemView.findViewById(R.id.address_edit_btn);
            setDefaultTxtv = (TextView) itemView.findViewById(R.id.address_default_txtv);
        }
    }

    public interface AddressListListener {

        void onItemEdit(SPConsigneeAddress consigneeAddress);

        void onItemDel(SPConsigneeAddress consigneeAddress);

        void onItemClick(SPConsigneeAddress consigneeAddress);

    }

}
