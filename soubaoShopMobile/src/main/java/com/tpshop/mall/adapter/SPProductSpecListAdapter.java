package com.tpshop.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPCommonUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.model.shop.SPProductSpec;
import com.tpshop.mall.widget.tagview.Tag;
import com.tpshop.mall.widget.tagview.TagListView;
import com.tpshop.mall.widget.tagview.TagListView.OnTagClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2016/6/23
 */
public class SPProductSpecListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> keys;
    private Collection<String> mSelectSpecs;
    private OnTagClickListener mTagClickListener;
    private Map<String, List<SPProductSpec>> mSpecGroupMap;

    public SPProductSpecListAdapter(Context context, OnTagClickListener tagClickListener, Collection<String> selectSpecs) {
        this.mContext = context;
        this.mTagClickListener = tagClickListener;
        keys = new ArrayList<>();
        this.mSelectSpecs = selectSpecs;
    }

    public void setData(Map<String, List<SPProductSpec>> specGroupMap) {
        if (specGroupMap == null) return;
        this.mSpecGroupMap = specGroupMap;
        keys.clear();
        Set<String> keySet = this.mSpecGroupMap.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            keys.add(key);
        }
    }

    @Override
    public int getCount() {
        if (keys == null) return 0;
        return keys.size();
    }

    @Override
    public Object getItem(int position) {
        if (keys == null) return null;
        return keys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        float titleHeight = 0;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_spproduct_list_filter_item, parent, false);
            holder.tagListv = (TagListView) convertView.findViewById(R.id.filter_taglstv);
            holder.tagListv.setTagViewBackgroundRes(R.drawable.tag_button_bg_unchecked);
            holder.tagListv.setTagViewTextColorRes(R.color.color_font_gray);
            holder.tagListv.setOnTagClickListener(mTagClickListener);
            holder.titleTxtv = (TextView) convertView.findViewById(R.id.filter_title_txtv);
            titleHeight = SPCommonUtils.dip2px(mContext, 20);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String key = keys.get(position);
        List<SPProductSpec> specs = mSpecGroupMap.get(key);
        List<Tag> tags = getTags(key, specs);
        holder.tagListv.setTags(tags);
        holder.titleTxtv.setText(key);
        float totalHeight = titleHeight + holder.tagListv.getTagListViewHeight();
        convertView.setMinimumHeight(Float.valueOf(totalHeight).intValue());
        return convertView;
    }

    private List<Tag> getTags(String key, List<SPProductSpec> specs) {
        if (specs == null) return null;
        List<Tag> mTags = new ArrayList<>();
        int size = specs.size();
        for (int i = 0; i < size; i++) {
            SPProductSpec spec = specs.get(i);
            Tag tag = new Tag();
            tag.setId(i);
            if (mSelectSpecs != null && mSelectSpecs.contains(spec.getItemID()))
                tag.setChecked(true);
            else
                tag.setChecked(false);
            tag.setKey(key);
            tag.setValue(spec.getItemID());
            tag.setTitle(spec.getItem());
            mTags.add(tag);
        }
        return mTags;
    }

    class ViewHolder {
        TextView titleTxtv;
        TagListView tagListv;
    }

}
