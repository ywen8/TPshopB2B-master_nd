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
 * Date: @date 2015年11月14日 下午8:17:18
 * Description: 商品列表排序, 筛选 标题栏
 *
 * @version V1.0
 */
package com.tpshop.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.global.SPMobileApplication;

public class SPProductFilterTabView extends FrameLayout implements View.OnClickListener {

    int mLastSortId;               //上一次选中的ID
    View pricelyout;
    View filterlyout;
    View salenumlyout;
    TextView priceTxt;
    TextView filterTxt;
    boolean isPriceSort;
    View compositelyout;
    TextView salenumTxt;
    TextView compositeTxt;
    ImageView priceDrameev;
    ImageView filterDrameev;
    private OnSortClickListener onSortClickListener;

    public SPProductFilterTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.spproduct_list_tab_view, this);
        compositelyout = view.findViewById(R.id.sort_composite_lyout);
        compositeTxt = (TextView) view.findViewById(R.id.sort_composite_txt);
        compositeTxt.setTextColor(getResources().getColor(R.color.sort_selected_text_color));
        compositelyout.setOnClickListener(this);
        salenumlyout = view.findViewById(R.id.sort_salenum_lyout);
        salenumTxt = (TextView) view.findViewById(R.id.sort_salenum_txt);
        salenumlyout.setOnClickListener(this);
        pricelyout = view.findViewById(R.id.sort_price_layout);
        priceDrameev = (ImageView) view.findViewById(R.id.sort_price_draweev);
        priceTxt = (TextView) view.findViewById(R.id.sort_price_txt);
        pricelyout.setOnClickListener(this);
        filterlyout = view.findViewById(R.id.sort_filter_layout);
        filterDrameev = (ImageView) view.findViewById(R.id.sort_filter_drawwee);
        filterTxt = (TextView) view.findViewById(R.id.sort_filter_txt);
        filterlyout.setOnClickListener(this);
        if (SPMobileApplication.getInstance().isFilterShow)
            filterlyout.setVisibility(View.VISIBLE);
        else
            filterlyout.setVisibility(View.GONE);
        view.invalidate();
        isPriceSort = false;
    }

    /**
     * 排序类型
     */
    public enum ProductSortType {
        composite,    //综合,默认
        salenum,      //销量
        price,        //价格
        filter        //筛选
    }

    @Override
    public void onClick(View v) {
        if (onSortClickListener == null) return;
        if (mLastSortId == v.getId() && (v.getId() == R.id.sort_salenum_lyout || v.getId() == R.id.sort_composite_lyout))
            return;
        if (v.getId() == R.id.sort_composite_lyout) {
            onSortClickListener.onFilterClick(ProductSortType.composite);
            refreshSortViewState(ProductSortType.composite);
        } else if (v.getId() == R.id.sort_salenum_lyout) {
            onSortClickListener.onFilterClick(ProductSortType.salenum);
            refreshSortViewState(ProductSortType.salenum);
        } else if (v.getId() == R.id.sort_price_layout) {
            onSortClickListener.onFilterClick(ProductSortType.price);
            refreshSortViewState(ProductSortType.price);
        } else if (v.getId() == R.id.sort_filter_layout) {
            onSortClickListener.onFilterClick(ProductSortType.filter);
            refreshSortViewState(ProductSortType.filter);
        }
        mLastSortId = v.getId();
    }

    private void refreshSortViewState(ProductSortType sortType) {
        compositeTxt.setTextColor(getResources().getColor(R.color.sort_normal_text_color));
        salenumTxt.setTextColor(getResources().getColor(R.color.sort_normal_text_color));
        priceTxt.setTextColor(getResources().getColor(R.color.sort_normal_text_color));
        filterTxt.setTextColor(getResources().getColor(R.color.sort_normal_text_color));
        priceDrameev.setImageResource(R.drawable.shop_icon_price_normal);
        isPriceSort = false;
        switch (sortType) {
            case composite:
                compositeTxt.setTextColor(getResources().getColor(R.color.sort_selected_text_color));
                break;
            case salenum:
                salenumTxt.setTextColor(getResources().getColor(R.color.sort_selected_text_color));
                break;
            case price:
                priceTxt.setTextColor(getResources().getColor(R.color.sort_selected_text_color));
                isPriceSort = true;
                break;
            case filter:
                filterTxt.setTextColor(getResources().getColor(R.color.sort_selected_text_color));
                break;
        }
    }

    public void setSort(boolean isDesc) {
        if (isPriceSort) {
            if (isDesc)
                priceDrameev.setImageResource(R.drawable.shop_icon_price_desc);
            else
                priceDrameev.setImageResource(R.drawable.shop_icon_price_asc);
        } else {
            priceDrameev.setImageResource(R.drawable.shop_icon_price_normal);
        }
    }

    /***
     * 排序item点击
     */
    public interface OnSortClickListener {
        void onFilterClick(ProductSortType sortType);
    }

    public void setOnSortClickListener(OnSortClickListener onSortClickListener) {
        this.onSortClickListener = onSortClickListener;
    }

}
