/*
 * tpshop
 * ============================================================================
 * * 版权所有 2015-2027 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: 飞龙  16/01/15 $
 * $description: 服务操数据配置获取
 */
package com.tpshop.mall.utils;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.model.SPServiceConfig;

import java.util.List;

/**
 * Created by admin on 2016/6/29
 */
public class SPServerUtils {

    private final static String CONFIG_QQ = "qq";
    private final static String CONFIG_PHONE = "phone";
    private final static String CONFIG_ADDRESS = "address";
    private final static String CONFIG_STORE_NAME = "store_name";
    private final static String CONFIG_POINT_RATE = "point_rate";
    private final static String CONFIG_KEY_IS_AUDIT = "app_test";
    private final static String CONFIG_HOT_KEYWORDS = "hot_keywords";
    private final static String CONFIG_KEY_SMS_TIME_OUT = "sms_time_out";

    /**
     * 获取联系客服QQ
     */
    public static String getCustomerQQ() {
        return getConfigValue(CONFIG_QQ);
    }

    /**
     * 获取商城名称
     */
    public static String getStoreName() {
        return getConfigValue(CONFIG_STORE_NAME);
    }

    /**
     * 是否正在审核
     */
    public static boolean isAudit() {
        String auditStr = getConfigValue(CONFIG_KEY_IS_AUDIT);
        return SPStringUtils.isEmpty(auditStr) || Integer.valueOf(auditStr) == 1;
    }

    /**
     * 获取积分抵扣金额
     */
    public static String getPointRate() {
        return getConfigValue(CONFIG_POINT_RATE);
    }

    /**
     * 根据名称获取配置的值
     */
    private static String getConfigValue(String name) {
        List<SPServiceConfig> serviceConfigs = SPMobileApplication.getInstance().getServiceConfigs();
        if (serviceConfigs != null && serviceConfigs.size() > 0) {
            for (SPServiceConfig config : serviceConfigs) {
                if (name.equals(config.getName()))
                    return config.getValue();
            }
        }
        return name;
    }

    /**
     * 售后收货地址
     */
    public static String getAddress() {
        return getConfigValue(CONFIG_ADDRESS);
    }

    /**
     * 售后客服电话
     */
    public static String getServicePhone() {
        return getConfigValue(CONFIG_PHONE);
    }

    /**
     * 搜索关键词
     */
    public static List<String> getHotKeyword() {
        List<String> hotwords = null;
        String hotword = getConfigValue(CONFIG_HOT_KEYWORDS);
        if (!SPStringUtils.isEmpty(hotword))
            hotwords = SPStringUtils.stringToList(hotword, "|");
        return hotwords;
    }

    /**
     * 发送短信验证码超时时间(单位:s)
     */
    public static int getSmsTimeOut() {
        String timeout = getConfigValue(CONFIG_KEY_SMS_TIME_OUT);
        if (!SPStringUtils.isEmpty(timeout)) {
            try {
                return Integer.valueOf(timeout);
            } catch (Exception e) {
                e.printStackTrace();
                return 120;
            }
        }
        return 0;
    }

}
