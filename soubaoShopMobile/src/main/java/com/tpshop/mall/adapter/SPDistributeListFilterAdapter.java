package com.tpshop.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.model.distribute.SPDistributeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zw on 2017/6/16
 */
public class SPDistributeListFilterAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> parentData;
    private OnItemclickListener mListener;
    private Map<String, List<SPDistributeType>> childData;

    public SPDistributeListFilterAdapter(Context context, OnItemclickListener mListener, List<String> parentData,
                                         Map<String, List<SPDistributeType>> childData) {
        this.context = context;
        this.mListener = mListener;
        this.parentData = parentData;
        this.childData = childData;
    }

    @Override
    public int getGroupCount() {
        return parentData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(parentData.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(parentData.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentHolder parentHolder;
        if (convertView == null) {
            parentHolder = new ParentHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.parent_layout, null);
            parentHolder.parentName = (TextView) convertView.findViewById(R.id.type_name);
            parentHolder.parentImg = (ImageView) convertView.findViewById(R.id.type_img);
            convertView.setTag(parentHolder);
        } else {
            parentHolder = (ParentHolder) convertView.getTag();
        }
        parentHolder.parentName.setText(parentData.get(groupPosition));
        if (isExpanded)
            parentHolder.parentImg.setImageResource(R.drawable.xuanze);
        else
            parentHolder.parentImg.setImageResource(R.drawable.arrow_right);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.child_layout, null);
            childHolder.childName = (TextView) convertView.findViewById(R.id.type_name);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        final String parentName = parentData.get(groupPosition);
        final SPDistributeType childType = childData.get(parentName).get(childPosition);
        childHolder.childName.setText(childType.getTypeName());
        childHolder.childName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.checkType(parentName, childType.getTypeId());
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void updateData(ArrayList<String> groupData, Map<String, List<SPDistributeType>> childData) {
        this.parentData = groupData;
        this.childData = childData;
        this.notifyDataSetChanged();
    }

    private class ParentHolder {
        TextView parentName;
        ImageView parentImg;
    }

    private class ChildHolder {
        TextView childName;
    }

    public interface OnItemclickListener {
        void checkType(String typeName, int typeId);
    }

}
