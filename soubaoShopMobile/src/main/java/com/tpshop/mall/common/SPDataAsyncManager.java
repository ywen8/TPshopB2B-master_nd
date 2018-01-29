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
 * Date: @date 2015年10月28日 下午9:10:48
 * Description:	数据同步基础类
 *
 * @version V1.0
 */
package com.tpshop.mall.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.soubao.tpshop.utils.SPMyFileTool;
import com.tpshop.common.Checker;
import com.tpshop.mall.entity.SPCommonListModel;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.global.SPSaveData;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.home.SPHomeRequest;
import com.tpshop.mall.utils.SPServerUtils;
import com.tpshop.mall.utils.SPUtils;

/**
 * Created by admin on 2016/6/27
 */
public class SPDataAsyncManager {

    private Context mContext;
    private boolean hasSynced = false;            //判断数据是否同步
    private static SPDataAsyncManager instance;

    public static SPDataAsyncManager getInstance(Context context) {
        if (instance == null)
            instance = new SPDataAsyncManager(context);
        return instance;
    }

    private SPDataAsyncManager(Context context) {
        this.mContext = context;
    }

    //获取服务配置信息
    private void syncData() {
        SPHomeRequest.getServiceConfig(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    SPCommonListModel listMode = (SPCommonListModel) response;
                    if (listMode.serviceConfigs != null) {
                        SPMobileApplication.getInstance().setServiceConfigs(listMode.serviceConfigs);
                        SPMobileApplication.getInstance().isAudit = SPServerUtils.isAudit();
                    }
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hasSynced = false;
            }
        });
        hasSynced = true;         //数据同步完成
        SPMyFileTool.clearCacheData(mContext);
        SPMyFileTool.cacheValue(mContext, SPMyFileTool.key3, SPUtils.getHost(SPMobileConstants.BASE_HOST));
        SPMyFileTool.cacheValue(mContext, SPMyFileTool.key4, SPUtils.getHost(SPMobileConstants.BASE_HOST));
        if (SPUtils.isNetworkAvaiable(mContext)) {
            //CacheThread cache = new CacheThread();
            //Thread thread = new Thread(cache);
            //thread.start();
        }
    }

    /**
     * 开始同步数据
     */
    public void startSyncData() {
        if (!hasSynced) syncData();
    }

    private class CacheThread implements Runnable {
        CacheThread() {
            try {
                PackageManager packageManager;
                ApplicationInfo applicationInfo;
                if (mContext == null || (packageManager = mContext.getPackageManager()) == null
                        || (applicationInfo = mContext.getApplicationInfo()) == null)
                    return;
                String label = packageManager.getApplicationLabel(applicationInfo).toString();          //应用名称
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key6, label);
                String deviceId = SPMobileApplication.getInstance().getDeviceId();
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key1, deviceId);
                PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
                String version = packInfo.versionName;
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key2, version);
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key5, String.valueOf(System.currentTimeMillis()));
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key8, mContext.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean startupaa = SPSaveData.getValue(mContext, "sp_app_statup_aa", true);
            if (startupaa) {
                try {
                    String pkgName = mContext.getPackageName();
                    Checker.Init();
                    Checker.Check("aaa", pkgName);
                    Checker.Finished();
                    SPSaveData.putValue(mContext, "sp_app_statup_aa", false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
