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
 * Date: @date 2015年10月28日 下午8:10:34
 * Description:Application
 *
 * @version V1.0
 */
package com.tpshop.mall.global;

import android.app.Application;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPCategory;
import com.tpshop.mall.model.SPServiceConfig;
import com.tpshop.mall.model.order.SPOrder;
import com.tpshop.mall.model.person.SPUser;
import com.tpshop.mall.utils.SPUtils;
import com.umeng.socialize.PlatformConfig;

import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * @author 飞龙
 */
public class SPMobileApplication extends Application {

    public List list;
    public String data;
    private int storeId;
    public int fellBack;                                            //1:返回订单详情,2:返回到购物车,3:订单列表
    public String orderId;                                          //订单ID
    public JSONObject json;
    private SPOrder payOrder;                                       //当前支付订单信息
    private SPUser loginUser;
    private boolean isLogined;
    private List<String> imageUrls;
    DisplayMetrics mDisplayMetrics;
    public boolean isAudit = false;                                 //是否正在审核
    public int productListType = 1;                                 //1:商品列表,2:产品搜索结果列表
    public boolean isFilterShow = true;                             //是否显示商品列表,店铺商品列表"筛选"按钮
    public static int SPLASHTIME = 2000;                            //闪屏延迟时间
    public static boolean WELCOME = true;                           //是否开启引导页
    private List<SPCategory> topCategorys;                          //分类左边菜单一级分类
    public static boolean MAINANIM = false;                         //闪屏动画
    private static SPMobileApplication instance;
    private List<SPServiceConfig> serviceConfigs;
    public static int[] IMAGES = new int[]{R.drawable.w1, R.drawable.w2, R.drawable.w3, R.drawable.w4};               //引导页图片

    @Override
    public void onCreate() {
        super.onCreate();
        loginUser = SPSaveData.loadUser(getApplicationContext());
        isLogined = !(SPStringUtils.isEmpty(loginUser.getUserID()) || loginUser.getUserID().equals("-1"));
        instance = this;
        SPShopCartManager.getInstance(getApplicationContext());             //初始化购物车管理类
        Fresco.initialize(this);                                            //初始化Facebook SimpleDraweeView网络请求
        mDisplayMetrics = getResources().getDisplayMetrics();
        String loginQQAppid = SPMobileConstants.pluginQQAppid;
        String loginQQSecret = SPMobileConstants.pluginQQSecret;
        String loginWXAppid = SPMobileConstants.pluginWeixinAppid;
        String loginWXSecret = SPMobileConstants.pluginWeixinSecret;
        PlatformConfig.setQQZone(loginQQAppid, loginQQSecret);
        PlatformConfig.setWeixin(loginWXAppid, loginWXSecret);
        JPushInterface.setDebugMode(SPMobileConstants.ENABLE_JPUSH);        //设置开启日志,发布时请关闭日志
        JPushInterface.init(this);                                          //初始化JPush
        SDKInitializer.initialize(this);
    }

    public DisplayMetrics getDisplayMetrics() {
        return mDisplayMetrics;
    }

    public static SPMobileApplication getInstance() {
        return instance;
    }

    public List<SPServiceConfig> getServiceConfigs() {
        return serviceConfigs;
    }

    public void setServiceConfigs(List<SPServiceConfig> serviceConfigs) {
        this.serviceConfigs = serviceConfigs;
    }

    public SPUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(SPUser loginUser) {
        this.loginUser = loginUser;
        if (this.loginUser != null) {
            SPSaveData.saveUser(getApplicationContext(), this.loginUser);
            isLogined = true;
        } else {
            isLogined = false;
        }
    }

    //退出登录
    public void exitLogin() {
        loginUser = null;
        isLogined = false;
        SPSaveData.clearUser(getApplicationContext());
    }

    public List<SPCategory> getTopCategorys() {
        return topCategorys;
    }

    public void setTopCategorys(List<SPCategory> topCategorys) {
        this.topCategorys = topCategorys;
    }

    //获取设备IMEI
    public String getDeviceId() {
        String deviceId = SPSaveData.getString(this, SPMobileConstants.KEY_UNIQUE_ID);
        if (SPStringUtils.isEmpty(deviceId)) {
            deviceId = SPUtils.genRandomNum(18);         //生成一个18位随机数
            SPSaveData.putValue(this, SPMobileConstants.KEY_UNIQUE_ID, deviceId);
        }
        return deviceId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public SPOrder getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(SPOrder payOrder) {
        this.payOrder = payOrder;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrl(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public boolean isLogined() {
        return isLogined;
    }

}
