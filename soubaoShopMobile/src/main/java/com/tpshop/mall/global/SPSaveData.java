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
 * Description:数据临时存储操作类
 *
 * @version V1.0
 */
package com.tpshop.mall.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.model.person.SPUser;

public class SPSaveData {

    private static SharedPreferences mShare = null;

    private static SharedPreferences getShared(Context context) {
        if (mShare == null)
            mShare = context.getSharedPreferences(SPMobileConstants.APP_NAME, Context.MODE_PRIVATE);
        return mShare;
    }

    public static String getString(Context context, String key) {
        return getShared(context).getString(key, "");
    }

    public static Integer getInteger(Context context, String key, int defaultValue) {
        return getShared(context).getInt(key, defaultValue);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return getShared(context).getFloat(key, defaultValue);
    }

    public static boolean getValue(Context context, String key, boolean defaultValue) {
        return getShared(context).getBoolean(key, defaultValue);
    }

    public static boolean getValue(Context context, String key) {
        return getShared(context).getBoolean(key, false);
    }

    public static void putValue(Context context, String key, String val) {
        Editor editor = getShared(context).edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static void cacheLocation(Context context, String lng, String lat, String address) {
        Editor editor = getShared(context).edit();
        editor.putString(SPMobileConstants.KEY_LONGITUDE, lng);
        editor.putString(SPMobileConstants.KEY_LATITUDE, lat);
        editor.putString(SPMobileConstants.KEY_LOCATION, address);
        editor.commit();
    }

    static SPUser loadUser(Context context) {
        SPUser user = new SPUser();
        user.setUserID(getShared(context).getString("userId", "-1"));
        user.setNickName(getShared(context).getString("nickName", ""));
        String couponCount = getShared(context).getString("couponCount", "0");
        String userMoney = getShared(context).getString("userMoney", "0");
        String payPoints = getShared(context).getString("payPoints", "0");
        String level = getShared(context).getString("level", "0");
        String levelName = getShared(context).getString("levelName", "0");
        String token = getShared(context).getString("token", "0");
        user.setCouponCount(couponCount);
        user.setUserMoney(userMoney);
        user.setPayPoints(payPoints);
        user.setLevel(Integer.parseInt(level));
        user.setLevelName(levelName);
        user.setToken(token);
        return user;
    }

    static void clearUser(Context context) {
        Editor editor = getShared(context).edit();
        editor.putString("userId", "-1");
        editor.putString("nickName", "");
        editor.putString("couponCount", "0");
        editor.putString("userMoney", "0");
        editor.putString("payPoints", "0");
        editor.putString("level", "0");
        editor.putString("levelName", "");
        editor.putString("token", "");
        editor.commit();
    }

    static void saveUser(Context context, SPUser user) {
        Editor editor = getShared(context).edit();
        editor.putString("userId", user.getUserID());
        editor.putString("nickName", user.getNickName());
        editor.putString("couponCount", String.valueOf(user.getCouponCount()));
        editor.putString("userMoney", String.valueOf(user.getUserMoney()));
        editor.putString("payPoints", String.valueOf(user.getPayPoints()));
        editor.putString("level", String.valueOf(user.getLevel()));
        editor.putString("levelName", String.valueOf(user.getLevelName()));
        editor.putString("token", user.getToken());
        editor.commit();
    }

    public static void saveAddress(Context context, SPConsigneeAddress address) {
        Editor editor = getShared(context).edit();
        editor.putString("address", address.getAddress());
        editor.putString("addressId", address.getAddressID());
        editor.putString("districtId", address.getDistrict());
        editor.commit();
    }

    public static SPConsigneeAddress getAddress(Context context) {
        SPConsigneeAddress address = new SPConsigneeAddress();
        String addressStr = getShared(context).getString("address", "");
        String addressId = getShared(context).getString("addressId", "");
        String districtId = getShared(context).getString("districtId", "");
        address.setAddress(addressStr);
        address.setAddressID(addressId);
        address.setDistrict(districtId);
        return address;
    }

    public static void putValue(Context context, String key, int val) {
        Editor editor = getShared(context).edit();
        editor.putInt(key, val);
        editor.commit();
    }

    public static void putValue(Context context, String key, long val) {
        Editor editor = getShared(context).edit();
        editor.putLong(key, val);
        editor.commit();
    }

    public static void putValue(Context context, String key, float val) {
        Editor editor = getShared(context).edit();
        editor.putFloat(key, val);
        editor.commit();
    }

    public static void putValue(Context context, String key, boolean val) {
        Editor editor = getShared(context).edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

}
 
