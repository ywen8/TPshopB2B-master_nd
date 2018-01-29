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
import android.widget.ExpandableListView;

import com.tpshop.mall.R;
import com.tpshop.mall.adapter.SPDistributeListFilterAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.distribute.SPDistributeType;
import com.tpshop.mall.utils.SPDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SPDistributeListFilterFragment extends Fragment implements SPDistributeListFilterAdapter.OnItemclickListener {

    private View mView;
    private static Handler mHandler;
    private ArrayList<String> groupData;
    private SPDistributeListFilterAdapter mFilterAdapter;
    private Map<String, List<SPDistributeType>> childData;
    private static SPDistributeListFilterFragment instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            initView(inflater, container);
            initData();
        }
        instance = this;
        return mView;
    }

    public static SPDistributeListFilterFragment getInstance(Handler handler) {
        mHandler = handler;
        return instance;
    }

    private void initData() {
        groupData.add("全部分类");
        groupData.add("全部品牌");
        SPShopRequest.getDistributeType(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                Map<String, List<SPDistributeType>> map = (Map<String, List<SPDistributeType>>) response;
                childData.put(groupData.get(0), map.get("categoryList"));
                childData.put(groupData.get(1), map.get("brandList"));
                mFilterAdapter.updateData(groupData, childData);
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                SPDialogUtils.showToast(getActivity(), msg);
            }
        });
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_spdistribute_list_filter, container, false);
        ExpandableListView expandablelistview = (ExpandableListView) mView.findViewById(R.id.expandablelistview);
        groupData = new ArrayList<>();
        childData = new HashMap<>();
        mFilterAdapter = new SPDistributeListFilterAdapter(getActivity(), this, groupData, childData);
        expandablelistview.setAdapter(mFilterAdapter);
    }

    @Override
    public void checkType(String typeName, int typeId) {
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_FILTER_CHANGE_ACTION);
            msg.obj = typeName + "," + typeId;
            mHandler.sendMessage(msg);
        }
    }

}
