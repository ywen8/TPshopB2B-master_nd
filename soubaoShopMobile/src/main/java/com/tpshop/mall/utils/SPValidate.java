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
 * $description:  验证类
 */
package com.tpshop.mall.utils;

import com.soubao.tpshop.utils.SPStringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/11/28
 */
public class SPValidate {

    /**
     * 验证密码:密码和确认密码必须是字母大小写或数字,长度为6-16位
     */
    public static boolean checkPassword(String pwd) {
        if (SPStringUtils.isEmpty(pwd)) return false;
        String pattern = "^[0-9A-Za-z]{6,20}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(pwd);
        return m.find();
    }

}
