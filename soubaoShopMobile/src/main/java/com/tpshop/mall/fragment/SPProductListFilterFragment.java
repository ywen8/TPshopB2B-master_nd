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
 * Date: @date 2015年10月20日 下午7:19:26
 * Description:SPPersonFragment 商品列表 -> 筛选View
 *
 * @version V1.0
 */
package com.tpshop.mall.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.shop.SPProductListActivity;
import com.tpshop.mall.activity.shop.SPProductListSearchResultActivity;
import com.tpshop.mall.adapter.SPProductListFilterAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.model.shop.SPFilter;
import com.tpshop.mall.widget.tagview.Tag;
import com.tpshop.mall.widget.tagview.TagListView;
import com.tpshop.mall.widget.tagview.TagView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SPProductListFilterFragment extends Fragment implements TagListView.OnTagCheckedChangedListener, TagListView.OnTagClickListener {

    private View mView;
    private List<SPFilter> mFilters;
    private static Handler mHandler;
    private SPProductListFilterAdapter mFilterAdapter;
    private static SPProductListFilterFragment instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null)
            initView(inflater, container);
        instance = this;
        return mView;
    }

    public static SPProductListFilterFragment getInstance(Handler handler) {
        mHandler = handler;
        return instance;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SPMobileApplication.getInstance().productListType == 1) {        //产品列表
            JSONObject dataJson = SPProductListActivity.getInstance().mDataJson;
            setDataSource(dataJson);
        } else {        //搜索结果列表
            JSONObject dataJson = SPProductListSearchResultActivity.getInstance().mDataJson;
            setDataSource(dataJson);
        }
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_spproduct_list_filter, container, false);
        ListView mFilterListv = (ListView) mView.findViewById(R.id.product_filter_lstv);
        mFilterAdapter = new SPProductListFilterAdapter(getActivity(), this);
        mFilterListv.setAdapter(mFilterAdapter);
    }

    public void setDataSource(JSONObject jsonObject) {
        if (jsonObject == null) return;
        if (mFilters == null)
            mFilters = new ArrayList<>();
        else
            mFilters.clear();
        try {
            if (jsonObject.has("menu")) {
                SPFilter menuFilter = (SPFilter) jsonObject.get("menu");
                mFilters.add(menuFilter);
            }
            if (jsonObject.has("filter")) {
                List<SPFilter> filters = (List<SPFilter>) jsonObject.get("filter");
                mFilters.addAll(filters);
            }
            mFilterAdapter.setData(mFilters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTagCheckedChanged(TagView tagView, Tag tag) {
    }

    @Override
    public void onTagClick(TagView tagView, Tag tag) {
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_FILTER_CHANGE_ACTION);
            msg.obj = tag.getValue();
            mHandler.sendMessage(msg);
        }
    }

}
