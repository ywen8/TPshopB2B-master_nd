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
 * Description:  积分金额明细 dapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.model.shop.SPGoodsComment;
import com.tpshop.mall.utils.SPUtils;
import com.tpshop.mall.widget.SPStarSmallView;

import java.util.List;

/**
 * @author 飞龙
 */
public class SPProductDetailCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Rect bounds = new Rect();
    private List<SPGoodsComment> mComments;
    private OnImageClickListener clickListener;

    public SPProductDetailCommentAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<SPGoodsComment> comments) {
        if (comments == null) return;
        this.mComments = comments;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.product_details_comment_list_item, parent, false);
        return new SPProductDetailCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        SPGoodsComment goodsComment = mComments.get(position);
        if (!SPStringUtils.isEmpty(goodsComment.getAddTime())) {
            String date = SPUtils.convertFullTimeFromPhpTime(Long.valueOf(goodsComment.getAddTime()), "yyyy-MM-dd");
            String specName = goodsComment.getSpecName();
            if (SPStringUtils.isEmpty(specName))
                holder.dateTxtv.setText(date);
            else
                holder.dateTxtv.setText(date + " " + specName);
        }
        holder.headImgv.setImageURI(SPUtils.getImageUri(mContext, SPUtils.getImageUrl(goodsComment.getHeadUrl())));
        String content = goodsComment.getContent();
        String string = SPStringUtils.isEmpty(goodsComment.getNickname()) ? "匿名用户" : goodsComment.getNickname();
        holder.usernameTxtv.setText(string);
        holder.contentTxtv.setText(content);
        int marginTop;
        marginTop = SPCommonUtils.dip2px(mContext, 5);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.userLl.getLayoutParams();
        lp.setMargins(0, marginTop, 0, 0);
        holder.userLl.setLayoutParams(lp);
        int textHeight = 0;
        if (!SPStringUtils.isEmpty(content)) {
            Paint paint = holder.contentTxtv.getPaint();
            paint.getTextBounds(content, 0, content.length(), bounds);
            textHeight = bounds.height();          //文本高度
        }
        int fixationHeight = SPCommonUtils.dip2px(mContext, 55);
        int itemHeight = fixationHeight + textHeight + marginTop;
        int imageHeight;
        if (goodsComment.getGoodsRank() != null) {
            int rank = Float.valueOf(goodsComment.getGoodsRank()).intValue();
            holder.starView.checkStart(rank - 1);
        }
        List<String> images = goodsComment.getImages();
        if (SPCommonUtils.verifyArray(images)) {
            buildProductGallery(holder.gallery, images);
            holder.scrollv.setVisibility(View.VISIBLE);
            imageHeight = SPCommonUtils.dip2px(mContext, 80);
            holder.itemView.setMinimumHeight(itemHeight);
        } else {
            imageHeight = 0;
            holder.scrollv.setVisibility(View.GONE);
            holder.itemView.setMinimumHeight(textHeight);
        }
        holder.itemView.setMinimumHeight(imageHeight);
    }

    @Override
    public int getItemCount() {
        return (mComments == null) ? 0 : mComments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View userLl;
        TextView dateTxtv;
        TextView contentTxtv;
        LinearLayout gallery;
        TextView usernameTxtv;
        SPStarSmallView starView;
        SimpleDraweeView headImgv;
        HorizontalScrollView scrollv;

        public ViewHolder(View itemView) {
            super(itemView);
            userLl = itemView.findViewById(R.id.user_ll);
            headImgv = (SimpleDraweeView) itemView.findViewById(R.id.head_mimgv);
            usernameTxtv = (TextView) itemView.findViewById(R.id.comment_username_txtv);
            dateTxtv = (TextView) itemView.findViewById(R.id.comment_addtime_txtv);
            contentTxtv = (TextView) itemView.findViewById(R.id.comment_content_txtv);
            starView = (SPStarSmallView) itemView.findViewById(R.id.comment_star_layout);
            starView.setStarImage(R.drawable.icon_detail_comment_normal, R.drawable.icon_detail_comment_checked);
            gallery = (LinearLayout) itemView.findViewById(R.id.comment_gallery_lyaout);
            scrollv = (HorizontalScrollView) itemView.findViewById(R.id.comment_product_scrollv);
        }
    }

    private void buildProductGallery(LinearLayout gallery, final List<String> images) {
        gallery.removeAllViews();
        if (SPCommonUtils.verifyArray(images)) {
            for (int i = 0; i < images.size(); i++) {
                final int position = i;
                String url = images.get(i);
                View view = LayoutInflater.from(mContext).inflate(R.layout.activity_index_gallery_item, gallery, false);
                ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
                Glide.with(mContext).load(url).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null) clickListener.onClickListener(images, position);
                    }
                });
                gallery.addView(view);
            }
        }
    }

    public void setOnImageListener(OnImageClickListener listener) {
        this.clickListener = listener;
    }

    public interface OnImageClickListener {
        void onClickListener(List<String> imgUrls, int position);
    }

}
