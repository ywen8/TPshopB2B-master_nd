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
 * Date: @date 2017年5月11日 下午10:03:56
 * Description: 购物车 adapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPUtils;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SPShopcartListAdapter extends SectionedRecyclerViewAdapter<SPShopcartListAdapter.HeaderViewHolder, SPShopcartListAdapter.ItemViewHolder,
        SPShopcartListAdapter.FooterViewHolder> {

    private Context mContext;
    private List<SPStore> mStores;
    private ShopCarClickListener mShopCarClickListener;

    public SPShopcartListAdapter(Context context, ShopCarClickListener shopCarClickListener) {
        this.mContext = context;
        this.mShopCarClickListener = shopCarClickListener;
    }

    public void updateData(List<SPStore> stores) {
        if (stores == null) stores = new ArrayList<>();
        this.mStores = stores;
        this.notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        if (mStores == null) return 0;
        return this.mStores.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        List<SPProduct> products;
        if (this.mStores.get(section) == null || (products = this.mStores.get(section).getStoreProducts()) == null)
            return 0;
        return products.size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_section_item, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    protected ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopcart_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    protected FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        final SPStore store = mStores.get(section);
        Glide.with(mContext).load(SPUtils.getImageUrl(store.getStoreLogo())).asBitmap().fitCenter().placeholder(R.drawable.icon_enter_store)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.storeLogo);
        holder.storeNameTxtv.setText(store.getStoreName());
        if ("1".equals(store.getSelected()))
            holder.storeCheckBtn.setBackgroundResource(R.drawable.icon_checked);
        else
            holder.storeCheckBtn.setBackgroundResource(R.drawable.icon_checkno);
        holder.storeCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShopCarClickListener != null)
                    mShopCarClickListener.onStoreCheck(store);           //店铺是否选中
            }
        });
        holder.storeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShopCarClickListener != null)
                    mShopCarClickListener.onStoreClick(store);            //进入店铺
            }
        });
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder holder, int section) {
    }

    @Override
    protected void onBindItemViewHolder(ItemViewHolder holder, int section, int position) {
        final SPProduct product = mStores.get(section).getStoreProducts().get(position);
        String spec = SPStringUtils.isEmpty(product.getSpecKeyName()) ? "规格:无" : product.getSpecKeyName();
        holder.nameTxtv.setText(product.getGoodsName());
        holder.specTxtv.setText(spec);
        holder.shopPriceTxtv.setText("¥" + product.getMemberGoodsPrice());
        holder.marketPriceTxtv.setText("¥" + product.getMarketPrice());
        holder.cartCountTxtv.setText(String.valueOf(product.getGoodsNum()));
        holder.marketPriceTxtv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (product.getStoreCount() > 0 && product.getSelected().equals("1"))
            holder.checkBtn.setBackgroundResource(R.drawable.icon_checked);
        else
            holder.checkBtn.setBackgroundResource(R.drawable.icon_checkno);
        String thumlUrl = product.getImageThumlUrl();
        Glide.with(mContext).load(thumlUrl).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv);
        if (product.getStoreCount() < 1)
            holder.nogoodImg.setVisibility(View.VISIBLE);
        else
            holder.nogoodImg.setVisibility(View.GONE);
        holder.checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = !product.getSelected().equals("1");
                if (mShopCarClickListener != null)
                    mShopCarClickListener.checkProductFromCart(product, checked);           //商品是否选中
            }
        });
        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShopCarClickListener != null)
                    mShopCarClickListener.minuProductFromCart(product);           //商品减少数量
            }
        });
        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShopCarClickListener != null)
                    mShopCarClickListener.plusProductFromCart(product);           //商品增加数量
            }
        });
        holder.picImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShopCarClickListener != null)
                    mShopCarClickListener.onDetailClick(product);           //进入商品详情
            }
        });
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShopCarClickListener != null)
                    mShopCarClickListener.deleteProductFromCart(product);           //删除商品
            }
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        Button plusBtn;
        Button minusBtn;
        Button checkBtn;
        TextView nameTxtv;
        ImageView picImgv;
        TextView specTxtv;
        ImageButton delBtn;
        ImageView nogoodImg;
        EditText cartCountTxtv;
        TextView shopPriceTxtv;
        TextView marketPriceTxtv;

        ItemViewHolder(View itemView) {
            super(itemView);
            nameTxtv = (TextView) itemView.findViewById(R.id.name_txtv);
            specTxtv = (TextView) itemView.findViewById(R.id.product_spec_txtv);
            picImgv = (ImageView) itemView.findViewById(R.id.pic_imgv);
            nogoodImg = (ImageView) itemView.findViewById(R.id.nogoodImg);
            shopPriceTxtv = (TextView) itemView.findViewById(R.id.shop_price_txtv);
            marketPriceTxtv = (TextView) itemView.findViewById(R.id.market_price_txtv);
            cartCountTxtv = (EditText) itemView.findViewById(R.id.cart_count_dtxtv);
            minusBtn = (Button) itemView.findViewById(R.id.cart_minus_btn);
            plusBtn = (Button) itemView.findViewById(R.id.cart_plus_btn);
            checkBtn = (Button) itemView.findViewById(R.id.check_btn);
            delBtn = (ImageButton) itemView.findViewById(R.id.del_btn);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView storeLogo;
        LinearLayout storeLl;
        Button storeCheckBtn;
        TextView storeNameTxtv;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            storeLl = (LinearLayout) itemView.findViewById(R.id.store_ll);
            storeLogo = (ImageView) itemView.findViewById(R.id.store_logo);
            storeCheckBtn = (Button) itemView.findViewById(R.id.store_check_btn);
            storeNameTxtv = (TextView) itemView.findViewById(R.id.store_name_txtv);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface ShopCarClickListener {

        void minuProductFromCart(SPProduct product);

        void plusProductFromCart(SPProduct product);

        void checkProductFromCart(SPProduct product, boolean checked);

        void deleteProductFromCart(SPProduct product);

        void onDetailClick(SPProduct product);

        void onStoreCheck(SPStore store);

        void onStoreClick(SPStore store);

    }

}
