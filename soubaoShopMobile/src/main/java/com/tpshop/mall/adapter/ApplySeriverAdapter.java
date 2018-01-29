package com.tpshop.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tpshop.mall.R;

import java.util.List;

/**
 * Created by zw on 2017/5/26
 */
public class ApplySeriverAdapter extends BaseAdapter {

    private List<Bitmap> datas;
    private AddPicOnclick addPicOnclick;
    private LayoutInflater layoutInflater;

    public ApplySeriverAdapter(Context context, List<Bitmap> datas, AddPicOnclick addPicOnclick) {
        this.layoutInflater = LayoutInflater.from(context);
        this.datas = datas;
        this.addPicOnclick = addPicOnclick;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.seriver_pic, null);
            holder.photo = (ImageView) convertView.findViewById(R.id.photo);
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.photo.setImageBitmap(datas.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.remove(position);
                ApplySeriverAdapter.this.notifyDataSetChanged();
                if (datas.size() < 5 && addPicOnclick != null) addPicOnclick.addPic(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView photo, delete;
    }

    public interface AddPicOnclick {
        void addPic(int position);
    }

}
