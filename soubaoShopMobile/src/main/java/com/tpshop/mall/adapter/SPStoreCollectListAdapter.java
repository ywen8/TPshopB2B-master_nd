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
 * Description:  店铺收藏列表 dapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPUtils;

import java.util.List;

/**
 * @author 飞龙
 */
public class SPStoreCollectListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<SPStore> mStores;
    private StoreCollectListener mListener;

    public SPStoreCollectListAdapter(Context context, StoreCollectListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void setData(List<SPStore> stores) {
        if (stores == null) return;
        this.mStores = stores;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.person_store_collect_list_item, null);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StoreViewHolder storeViewHolder = (StoreViewHolder) holder;
        final SPStore store = mStores.get(position);
        Glide.with(mContext).load(SPUtils.getImageUrl(store.getStoreLogo())).asBitmap().fitCenter().placeholder(R.drawable.category_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(storeViewHolder.storeImgv);
        if (!SPStringUtils.isEmpty(store.getStoreName()))
            storeViewHolder.storeNameTxtv.setText(store.getStoreName());
        storeViewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onCancelStore(store);
            }
        });
        storeViewHolder.storeImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onDetailStore(store);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mStores == null) return 0;
        return mStores.size();
    }

    private class StoreViewHolder extends RecyclerView.ViewHolder {
        Button delBtn;                  //取消收藏
        ImageView storeImgv;            //店铺图片
        TextView storeNameTxtv;         //品牌名称

        StoreViewHolder(View itemView) {
            super(itemView);
            storeImgv = (ImageView) itemView.findViewById(R.id.store_imgv);
            storeNameTxtv = (TextView) itemView.findViewById(R.id.store_name_txtv);
            delBtn = (Button) itemView.findViewById(R.id.delete_btn);
        }
    }

    public interface StoreCollectListener {

        void onCancelStore(SPStore store);       //取消收藏

        void onDetailStore(SPStore store);       //店铺详情

    }

}
