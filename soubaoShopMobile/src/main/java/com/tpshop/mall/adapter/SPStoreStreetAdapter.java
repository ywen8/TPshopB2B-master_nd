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
 * Description: 店鋪街 adapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPShopUtils;
import com.tpshop.mall.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPStoreStreetAdapter extends RecyclerView.Adapter<SPStoreStreetAdapter.StreetViewHolder> {

    private Context mContext;
    private List<SPStore> mStoreLst;
    private StoreStreetListener mListener;

    public SPStoreStreetAdapter(Context context, StoreStreetListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void updateData(List<SPStore> storeList) {
        if (storeList == null)
            storeList = new ArrayList<>();
        this.mStoreLst = storeList;
        this.notifyDataSetChanged();
    }

    @Override
    public SPStoreStreetAdapter.StreetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.store_street_item, parent, false);
        return new SPStoreStreetAdapter.StreetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SPStoreStreetAdapter.StreetViewHolder holder, int position) {
        final SPStore store = mStoreLst.get(position);
        Glide.with(mContext).load(SPUtils.getImageUrl(store.getStoreLogo())).asBitmap().fitCenter().placeholder(R.drawable.category_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.storeLogoImgv);
        String storeName = (SPStringUtils.isEmpty(store.getStoreName())) ? "店铺名称异常" : store.getStoreName();
        holder.storeNameTxtv.setText(storeName);
        holder.attentionCount.setText(store.getStoreCollect() + "人关注");
        String desccredit = "综合评分:" + store.getDesccredit();
        SpannableString desccreditSpanStr = getSpanStr(desccredit);
        holder.scoreTxtv.setText(desccreditSpanStr);
        holder.distanceTxtv.setText("距离店家" + store.getDistance() + "km");
        List<SPProduct> products = store.getStoreProducts();
        if (store.getIsOwnShop() == 1) {      //自营这一步必须要做,否则不会显示
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_own_shop);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.storeNameTxtv.setCompoundDrawables(drawable, null, null, null);
        } else {
            holder.storeNameTxtv.setCompoundDrawables(null, null, null, null);
        }
        if (products == null || products.size() < 1) {
            holder.recommandCbanner.setVisibility(View.GONE);
        } else {
            holder.recommandCbanner.setVisibility(View.VISIBLE);
            setRecommendGoods(holder.recommandCbanner, store.getStoreProducts());
        }
        holder.intoStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onEnterStoreClick(store.getStoreId());
            }
        });
        holder.distanceTxtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onStoreMapClick(store);
            }
        });
    }

    private SpannableString getSpanStr(String textFmt) {
        SpannableString scoreSpanStr = new SpannableString(textFmt);
        scoreSpanStr.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.light_red)), 5, textFmt.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);           //设置区域内文字颜色为洋红色
        return scoreSpanStr;
    }

    /**
     * 设置推荐商品
     */
    private void setRecommendGoods(ConvenientBanner recommandCbanner, final List<SPProduct> datas) {
        List<List<SPProduct>> handledData = SPShopUtils.handleRecommendGoods(datas);
        recommandCbanner.setManualPageable(handledData.size() != 1);        //设置如果只有一组数据时不能滑动
        recommandCbanner.setCanLoop(handledData.size() != 1);
        recommandCbanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new ItemRecommendAdapter();
            }
        }, handledData);
    }

    @Override
    public int getItemCount() {
        if (mStoreLst == null) return 0;
        return mStoreLst.size();
    }

    class StreetViewHolder extends RecyclerView.ViewHolder {
        TextView scoreTxtv;                         //综合评分
        Button intoStoreBtn;                        //进入店铺
        TextView ownShopTxtv;                       //是否自营
        TextView distanceTxtv;                      //店铺距离
        TextView storeNameTxtv;                     //店铺名称
        ImageView storeLogoImgv;                    //店铺LOGO
        TextView attentionCount;                    //关注人数
        ConvenientBanner recommandCbanner;

        StreetViewHolder(View itemView) {
            super(itemView);
            storeLogoImgv = (ImageView) itemView.findViewById(R.id.store_logo_imgv);
            intoStoreBtn = (Button) itemView.findViewById(R.id.into_store_btn);
            attentionCount = (TextView) itemView.findViewById(R.id.attention_txtv);
            scoreTxtv = (TextView) itemView.findViewById(R.id.score_txtv);
            storeNameTxtv = (TextView) itemView.findViewById(R.id.store_name_txtv);
            ownShopTxtv = (TextView) itemView.findViewById(R.id.own_shop_txtv);
            distanceTxtv = (TextView) itemView.findViewById(R.id.distance_txtv);
            recommandCbanner = (ConvenientBanner) itemView.findViewById(R.id.street_recommand_cbanner);
        }
    }

    public interface StoreStreetListener {

        void onEnterStoreClick(int storeId);        //进入店铺

        void onStoreMapClick(SPStore store);        //进入店铺地图

    }

}
