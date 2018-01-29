package com.tpshop.mall.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tpshop.mall.activity.person.address.SPCitySelectActivity;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.model.person.SPRegionModel;

import java.util.List;

/**
 * Created by admin on 2016/7/9
 */
public class SPCityUtil {

    private Context context;
    private Handler handler;

    public SPCityUtil(final Context context, final Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    /***
     * 初始化省份
     */
    public void initProvince() {
        SPPersonRequest.getRegion("0", new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                List<SPRegionModel> regions = (List<SPRegionModel>) response;        //省份
                Message message = handler.obtainMessage(SPCitySelectActivity.LEVEL_PROVINCE);
                message.obj = regions;
                handler.sendMessage(message);
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                SPDialogUtils.showToast(context, msg);
            }
        });
    }

    /**
     * 获取下级地区
     */
    public void initChildrenRegion(final String parentID, final int level) {
        SPPersonRequest.getRegion(parentID, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                List<SPRegionModel> regions = (List<SPRegionModel>) response;        //下级地区
                Message message = handler.obtainMessage(level);
                message.obj = regions;
                handler.sendMessage(message);
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                SPDialogUtils.showToast(context, msg);
            }
        });
    }

}
