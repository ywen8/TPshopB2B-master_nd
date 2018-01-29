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
 * Date: @date 2017年3月5日 下午9:52:45
 * Description: 店铺 -> 首页 adapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.shop.SPStoreHomeActivity;
import com.tpshop.mall.activity.shop.SPStoreProductListActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPStoreRequest;
import com.tpshop.mall.model.SPHomeCategory;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPUtils;
import com.tpshop.mall.widget.CustomToast;
import com.tpshop.mall.widget.SPDrawableHorizontalButton;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SPStoreHomeAdapter extends SectionedRecyclerViewAdapter<SPStoreHomeAdapter.HeaderViewHolder, SPStoreHomeAdapter.GoodsItemViewHolder,
        SPStoreHomeAdapter.FooterViewHolder> implements View.OnClickListener {

    private SPStore mStore;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<SPHomeCategory> mDatas;
    private List<SPProduct> mHomeProducts;
    private OnItemClickListener mListener;

    public SPStoreHomeAdapter(Context ctx, OnItemClickListener listener) {
        this.mContext = ctx;
        this.mListener = listener;
    }

    public List<SPProduct> getHomeProduct() {
        return mHomeProducts;
    }

    public void updateData(SPStore store, List<SPHomeCategory> products) {
        if (products == null) products = new ArrayList<>();
        mHomeProducts = new ArrayList<>();
        this.mStore = store;
        this.mDatas = products;
        this.notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        if (mDatas != null) {
            if (mDatas.size() > 0)
                return mDatas.size();
            else
                return 1;
        }
        return 0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (mDatas != null && mDatas.size() > 0 && mDatas.get(section).getGoodsList() != null)
            return mDatas.get(section).getGoodsList().size();
        return 0;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.store_home_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    protected FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.store_home_footer, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    protected GoodsItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.product_recy_item, parent, false);
        return new GoodsItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(final HeaderViewHolder holder, int section) {
        SPHomeCategory homeCategory = null;
        if (mDatas.size() > 0) {
            homeCategory = mDatas.get(section);
            holder.titleRl.setVisibility(View.VISIBLE);
        }
        if (section == 0) {
            holder.storeHeadView.setVisibility(View.VISIBLE);
            holder.storeName.setText(mStore.getStoreName());
            holder.storeCount.setText(mStore.getTotalGoods() + "\n全部商品");
            holder.newTxtv.setText(mStore.getNewGoods() + "\n上新");
            holder.hotTxtv.setText(mStore.getHotGoods() + "\n热销");
            holder.storeAttention.setText(mStore.getStoreCollect() + "人关注");
            String logoUrl = SPMobileConstants.BASE_HOST + mStore.getStoreLogo();
            Glide.with(mContext).load(logoUrl).asBitmap().fitCenter().placeholder(R.drawable.category_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.storeLogo);
            List<String> imgUrls = getLoopBannerUrl();              //广告轮播
            holder.bannerLyaout.setVisibility(View.GONE);           //暂时屏蔽
            holder.storeBanner.setPages(new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new NetworkImageHolderView();
                }
            }, imgUrls);
            holder.storeBanner.setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    mListener.onItemAdClick(position);
                }
            });
            if (mStore.getIsCollect() == 1) {      //已关注
                holder.attentionDhbtn1.setVisibility(View.GONE);
                holder.attentionDhbtn2.setVisibility(View.VISIBLE);
            } else {      //未关注
                holder.attentionDhbtn1.setVisibility(View.VISIBLE);
                holder.attentionDhbtn2.setVisibility(View.GONE);
            }
            holder.storeCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStore.getTotalGoods() > 0) {
                        Intent intent = new Intent(mContext, SPStoreProductListActivity.class);
                        intent.putExtra("storeId", mStore.getStoreId());
                        mContext.startActivity(intent);
                    }
                }
            });
            holder.newTxtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStore.getNewGoods() > 0) {
                        Intent intent = new Intent(mContext, SPStoreProductListActivity.class);
                        intent.putExtra("storeId", mStore.getStoreId());
                        intent.putExtra("type", "is_new");
                        mContext.startActivity(intent);
                    }
                }
            });
            holder.hotTxtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStore.getHotGoods() > 0) {
                        Intent intent = new Intent(mContext, SPStoreProductListActivity.class);
                        intent.putExtra("storeId", mStore.getStoreId());
                        intent.putExtra("type", "is_hot");
                        mContext.startActivity(intent);
                    }
                }
            });
            holder.attentionDhbtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {       //关注
                    SPStoreRequest.collectOrCancelStoreWithID(mStore.getStoreId(), new SPSuccessListener() {
                        @Override
                        public void onRespone(String msg, Object response) {
                            holder.attentionDhbtn1.setVisibility(View.GONE);
                            holder.attentionDhbtn2.setVisibility(View.VISIBLE);
                            CustomToast.getToast().ToastShow(mContext, msg, R.drawable.success);
                        }
                    }, new SPFailuredListener((SPStoreHomeActivity) mContext) {
                        @Override
                        public void onRespone(String msg, int errorCode) {
                            CustomToast.getToast().ToastShow(mContext, msg, R.drawable.fail);
                        }
                    });
                }
            });
            holder.attentionDhbtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {       //取消关注
                    SPStoreRequest.collectOrCancelStoreWithID(mStore.getStoreId(), new SPSuccessListener() {
                        @Override
                        public void onRespone(String msg, Object response) {
                            holder.attentionDhbtn1.setVisibility(View.VISIBLE);
                            holder.attentionDhbtn2.setVisibility(View.GONE);
                            CustomToast.getToast().ToastShow(mContext, msg, R.drawable.success);
                        }
                    }, new SPFailuredListener((SPStoreHomeActivity) mContext) {
                        @Override
                        public void onRespone(String msg, int errorCode) {
                            CustomToast.getToast().ToastShow(mContext, msg, R.drawable.fail);
                        }
                    });
                }
            });
        } else {
            holder.storeHeadView.setVisibility(View.GONE);
        }
        if (homeCategory != null)
            holder.sectionTitleTxtv.setText(homeCategory.getName());
    }

    //广告轮播
    private List<String> getLoopBannerUrl() {
        List<String> imgUrls = new ArrayList<>();
        if (!SPStringUtils.isEmpty(mStore.getSlideUrl())) {
            String[] slides = mStore.getSlideUrl().split(",");
            for (String slide : slides) {
                if (SPStringUtils.isEmpty(slide)) continue;
                imgUrls.add(SPUtils.getImageUrl(slide));
            }
        }
        return imgUrls;
    }

    @Override
    protected void onBindItemViewHolder(GoodsItemViewHolder holder, int section, int position) {
        if (mDatas.size() > 0) {
            SPHomeCategory item = mDatas.get(section);
            final SPProduct product = item.getGoodsList().get(position);
            mHomeProducts.add(product);
            holder.nameTxtv.setText(product.getGoodsName());
            String price = "¥" + product.getShopPrice();
            SpannableString priceSpanStr = new SpannableString(price);
            float fontSize = this.mContext.getResources().getDimension(R.dimen.textSizeSmall);
            priceSpanStr.setSpan(new RelativeSizeSpan(fontSize), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.priceTxtv.setText(price);
            holder.priceTxtv.setTag(section);
            String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, 400, 400, product.getGoodsID());
            Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) mListener.onItemClick(product);
                }
            });
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder holder, int section) {
        if (mDatas.size() <= 0)
            holder.moreRl.setVisibility(View.GONE);
        holder.moreRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onStoreGoodsAllClick();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.store_count_txtv:
                if (mListener == null) return;
                mListener.onStoreGoodsAllClick();
                break;
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView newTxtv;                                         //上新商品
        TextView hotTxtv;                                         //热销商品
        TextView storeName;                                       //店铺名称
        ImageView storeLogo;                                      //店铺logo
        TextView storeCount;                                      //全部商品
        RelativeLayout titleRl;
        TextView storeAttention;                                  //关注量
        TextView sectionTitleTxtv;
        LinearLayout storeHeadView;                               //店铺信息头部
        RelativeLayout bannerLyaout;                              //广告位
        ConvenientBanner storeBanner;                             //广告图
        SPDrawableHorizontalButton attentionDhbtn1;               //关注
        SPDrawableHorizontalButton attentionDhbtn2;               //已关注

        public HeaderViewHolder(View itemView) {
            super(itemView);
            storeHeadView = (LinearLayout) itemView.findViewById(R.id.store_head_view);
            storeLogo = (ImageView) itemView.findViewById(R.id.store_logo_imgv);
            storeAttention = (TextView) itemView.findViewById(R.id.store_attention_txtv);
            storeName = (TextView) itemView.findViewById(R.id.store_name_txtv);
            attentionDhbtn1 = (SPDrawableHorizontalButton) itemView.findViewById(R.id.attention_dhbtn1);
            attentionDhbtn2 = (SPDrawableHorizontalButton) itemView.findViewById(R.id.attention_dhbtn2);
            storeCount = (TextView) itemView.findViewById(R.id.store_count_txtv);
            newTxtv = (TextView) itemView.findViewById(R.id.new_txtv);
            hotTxtv = (TextView) itemView.findViewById(R.id.hot_txtv);
            bannerLyaout = (RelativeLayout) itemView.findViewById(R.id.banner_lyaout);
            storeBanner = (ConvenientBanner) itemView.findViewById(R.id.store_banner_cbanner);
            titleRl = (RelativeLayout) itemView.findViewById(R.id.title_rl);
            sectionTitleTxtv = (TextView) itemView.findViewById(R.id.section_title_txtv);
            //设置两个点图片作为翻页指示器,不设置则没有指示器,可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
            storeBanner.setPageIndicator(new int[]{R.mipmap.index_white, R.mipmap.index_red});
            storeBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout moreRl;

        public FooterViewHolder(View itemView) {
            super(itemView);
            moreRl = (RelativeLayout) itemView.findViewById(R.id.more_rl);
        }
    }

    class GoodsItemViewHolder extends RecyclerView.ViewHolder {
        ImageView picImgv;
        Button similarBtn;
        TextView nameTxtv;             //商品名称
        TextView priceTxtv;            //商品价格

        GoodsItemViewHolder(View itemView) {
            super(itemView);
            nameTxtv = (TextView) itemView.findViewById(R.id.product_name_txtv);
            priceTxtv = (TextView) itemView.findViewById(R.id.product_price_txtv);
            picImgv = (ImageView) itemView.findViewById(R.id.product_pic_imgv);
            similarBtn = (Button) itemView.findViewById(R.id.similar_btn);
            similarBtn.setVisibility(View.GONE);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(SPProduct product);

        void onItemAdClick(int position);

        void onStoreGoodsAllClick();            //全部商品

    }

}
