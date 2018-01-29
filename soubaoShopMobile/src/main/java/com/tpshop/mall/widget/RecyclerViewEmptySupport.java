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
 * Date: @date 2017年5月9日 下午9:10:48
 * Description: 自定义RecyclerView , 支持setEmptyView
 *
 * @version V2.0
 */
package com.tpshop.mall.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.headerfooter.songhang.library.SmartRecyclerAdapter;

public class RecyclerViewEmptySupport extends RecyclerView {

    private View emptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            adapterChange();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        }
    };

    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null)
            adapter.registerAdapterDataObserver(emptyObserver);
        emptyObserver.onChanged();
    }

    private void adapterChange() {
        Adapter<?> adapter = getAdapter();
        int wrapperItemCount;
        if (adapter instanceof SmartRecyclerAdapter) {
            SmartRecyclerAdapter smartAdapter = (SmartRecyclerAdapter) adapter;
            wrapperItemCount = smartAdapter.getWrappedAdapter().getItemCount();
        } else {
            wrapperItemCount = adapter.getItemCount();
        }
        if (adapter != null && emptyView != null) {
            if (wrapperItemCount == 0) {
                emptyView.setVisibility(View.VISIBLE);
                RecyclerViewEmptySupport.this.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
            }
        }
    }

    public void notifyAdapterChange() {
        adapterChange();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

}
