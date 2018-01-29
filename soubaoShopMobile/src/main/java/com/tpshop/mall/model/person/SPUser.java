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
 * Date: @date 2015年10月27日 下午9:14:42
 * Description: 用户  model
 *
 * @version V1.0
 */
package com.tpshop.mall.model.person;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by admin on 2016/6/21
 */
public class SPUser implements SPModel, Serializable {

    private int level;
    private String sex;                           //性别
    private int waitPay;
    private String email;
    private String oauth;                         //第三方来源 wx weibo alipay
    private String token;
    private int waitSend;
    private String userID;
    private String mobile;
    private String openid;                        //第三方唯一标识
    private int cartCount;                        //购物车商品数量
    private String headPic;                       //头像URL
    private int focusCount;
    private int visitCount;
    private int returnCount;
    private String password;
    private int waitReceive;
    private String birthday;                      //出生日期
    private String nickName;                      //昵称
    private int commentCount;                     //评论数
    private int collectCount;
    private String userMoney;                     //用户金额
    private String payPoints;                     //消费积分
    private String levelName;                     //会员等级昵称
    private String checkCode;                     //校验码
    private String password2;                     //确认密码
    private int serviceCount;                     //待评论服务数量
    private String couponCount;                   //优惠券数量
    private int unCommentCount;                   //待评价数量
    private String frozenMoney = "0";

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "userID", "user_id",
                "userMoney", "user_money",
                "payPoints", "pay_points",
                "headPic", "head_pic",
                "nickName", "nickname",
                "level", "level",
                "couponCount", "coupon_count",
                "levelName", "level_name",
                "collectCount", "collect_count",
                "focusCount", "focus_count",
                "visitCount", "visit_count",
                "returnCount", "return_count",
                "frozenMoney", "frozen_money",
                "waitPay", "waitPay",
                "waitSend", "waitSend",
                "waitReceive", "waitReceive",
                "commentCount", "comment_count",
                "unCommentCount", "uncomment_count",
                "serviceCount", "serve_comment_count",
                "cartCount", "cart_goods_num",
        };
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(String userMoney) {
        this.userMoney = userMoney;
    }

    public String getPayPoints() {
        return payPoints;
    }

    public void setPayPoints(String payPoints) {
        this.payPoints = payPoints;
    }

    public String getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(String couponCount) {
        this.couponCount = couponCount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSex() {
        if (SPStringUtils.isEmpty(sex)) {
            return "0";
        }
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOauth() {
        return oauth;
    }

    public void setOauth(String oauth) {
        this.oauth = oauth;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        if (SPStringUtils.isEmpty(token)) {
            return;
        }
        this.token = token;
    }

    public String getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(String frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    public int getWaitPay() {
        return waitPay;
    }

    public void setWaitPay(int waitPay) {
        this.waitPay = waitPay;
    }

    public int getWaitSend() {
        return waitSend;
    }

    public void setWaitSend(int waitSend) {
        this.waitSend = waitSend;
    }

    public int getWaitReceive() {
        return waitReceive;
    }

    public void setWaitReceive(int waitReceive) {
        this.waitReceive = waitReceive;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getFocusCount() {
        return focusCount;
    }

    public void setFocusCount(int focusCount) {
        this.focusCount = focusCount;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public int getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(int returnCount) {
        this.returnCount = returnCount;
    }

    public int getUnCommentCount() {
        return unCommentCount;
    }

    public void setUnCommentCount(int unCommentCount) {
        this.unCommentCount = unCommentCount;
    }

    public int getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(int serviceCount) {
        this.serviceCount = serviceCount;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

}
