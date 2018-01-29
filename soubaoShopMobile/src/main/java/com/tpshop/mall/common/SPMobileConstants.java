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
 * Description:	系统使用的常量
 *
 * @version V1.0
 */
package com.tpshop.mall.common;

/**
 * @author 飞龙
 */
public class SPMobileConstants {


    public static final boolean DevTest = true;                                                     //发布是改成false
    public static final boolean IsRelease = true;                                                   //发布的时候改成false
    public static final boolean ENABLE_JPUSH = true;                                                //发布改成false
    public static final boolean ENABLE_SMS_CODE = true;                                             //启用短信验证码
    public static final boolean IS_DISTRIBUTION = false;                                             //是否开放分销
    public static final String BASE_HOST = "http://www.gdzxshop.com";// "http://120.25.197.108:8000/";                            //URL


    public static final String BASE_URL = BASE_HOST + "/index.php?m=api";
    public static final String BASE_URL_PREFIX = BASE_HOST + "/index.php?m=";
    public static final String FLEXIBLE_THUMBNAIL = BASE_HOST + "/index.php?m=Api&c=Goods&a=goodsThumImages&width=%d&height=%d&goods_id=%s";
    //查看物流
    public static final String SHIPPING_URL = BASE_HOST + "/api/User/express/order_id/%s.html";
    //关于我们
    public static final String URL_ABOUT_US = BASE_HOST ;// + "/index.php?m=api&c=user&a=about_us";
    //退换货列表
    public static final String URL_RETURN_LIST = BASE_HOST + "/index.php?m=api&c=order&a=return_goods_index";
    //售后详情
    public static final String URL_RETURN_DETAIL = BASE_HOST + "/index.php?m=api&c=order&a=return_goods_info";
    //账号/余额明细
    public static final String URL_ACCOUNT_HOSTORY = BASE_HOST + "/index.php?m=Api&c=User&a=account_list";
    //积分明细
    public static final String URL_POINT_HISTORY = BASE_HOST + "/index.php?m=Api&c=User&a=points_list";
    //充值记录
    public static final String URL_RECHARGE_HISTORY = BASE_HOST + "/index.php?m=Api&c=User&a=recharge_list";
    //提现记录
    public static final String URL_WITHDRAW_HISTORY = BASE_HOST + "/index.php?m=Api&c=User&a=withdrawals_list";
    //商品详情内容
    public static final String URL_GOODS_DETAIL_CONTENT = BASE_HOST + "/index.php?m=api&c=goods&a=goodsContent&id=%s";
    //入驻首页
    public static final String URL_NEWJOIN_AGREEMENT = BASE_HOST + "/index.php?m=Api&c=Newjoin&a=agreement";
    public static final String APP_NAME = "tpshop";
    public static final String KEY_SEARCH_KEY = "search_key";                                       //搜索关键词key
    public static final String KEY_NAME_KEY = "name_key";                                           //发票单位名称key
    public static final String KEY_CODE_KEY = "code_key";                                           //发票纳税人识别号key
    public static final String KEY_STORE_ID = "store_id";                                           //店铺ID
    public static final String KEY_WEB_URL = "web_url";                                             //请求URL
    public static final String KEY_WEB_TITLE = "web_title";                                         //title
    public static final String KEY_UNIQUE_ID = "unique_id";                                         //唯一设备ID
    public static final String KEY_CART_COUNT = "cart_count";                                       //购物车数量
    public static String KEY_HEAD_PIC = "head_pic";                                                 //用户头像
    /****系统所使用到的广播***/
    public static final String ACTION_SHOPCART_CHNAGE = "com.tpshop.mall.shoprcart_change";
    public static final String ACTION_LOGIN_CHNAGE = "com.tpshop.mall.login_change";                //登录状态改变
    public static final String ACTION_SPEC_CHNAGE = "com.tpshop.mall.spec_change";                  //商品规格改变
    public static final String ACTION_GOODS_RECOMMEND = "com.tpshop.mall.recommend";                //推荐商品
    public static final String ACTION_CONTRIBUTE_CHANGE = "com.tpshop.mall.contribute_change";      //分销中心设置修改
    public static final String ACTION_COUPON_CHANGE = "com.tpshop.mall.coupon_change";              //优惠改变(券使用或领券)广播
    public static final String ACTION_ORDER_CHANGE = "com.tpshop.malls.order_change";               //订单状态改变广播
    public static final String ACTION_STORE_CHANGE = "com.tpshop.malls.store_change";               //店铺收藏状态改变广播
    public static final String ACTION_COMMENT_CHANGE = "com.tpshop.malls.comment_change";           //商品评论状态改变广播
    /****系统常量*********/
    public static final String SP_SIGN_PRIVATGE_KEY = "tpshop2";                                    //签名KEY,同服务器一致
    public static final String SP_AUTH_CODE = "TPSHOP";                                             //授权code,同服务器一致,否则登录失败
    /****经度,纬度*********/
    public static final String KEY_RADIUS = "radius";
    public static final String KEY_LONGITUDE = "longitude";                                         //经度
    public static final String KEY_LATITUDE = "latitude";                                           //纬度
    public static final String KEY_LOCATION = "location_key";                                       //定位的城市
    public static final String KEY_LOCATION_ADDRESS = "location_address";                           //选择的城市
    //每页显示数据条数
    public static final int SizeOfPage = 15;
    public static final int CacheMessageCount = 50;                                                 //缓存消息数量,默认50条,如需要调整数量,可修改该值
    public static final int MSG_CODE_ORDER_BUTTON_ACTION = 10;
    public static final int MSG_CODE_ORDER_LIST_ITEM_ACTION = 11;
    public static final int MSG_CODE_FILTER_CHANGE_ACTION = 12;
    public static final int MSG_CODE_STORE_HOME_ACTION = 140;
    public static final int MSG_CODE_SEARCHKEY = 20;                                                //镇/街道
    public static final int MSG_CODE_AFTERSALE = 21;                                                //申请售后
    public static final int MSG_CODE_COMMENT = 22;                                                  //去评价
    /******** Activity启动相关 ***************/
    public static final int Result_Code_GetValue = 100;
    public static final int Result_Code_Refresh = 101;
    public static final int Result_Code_GetAddress = 102;
    public static final int Result_Code_GetPicture = 103;                                           //获取图片
    public static final int Result_Code_GetCoupon = 104;
    public static final int Result_Code_GetInvoce = 105;
    public static final int Result_Code_GetSelerMessage = 106;                                      //买家留言
    public static final int Result_Code_Login_Refresh = 108;                                        //进入登录页
    public static final int Result_Code_PAY = 109;                                                  //去支付
    public static final int tagCancel = 666;                                                        //取消订单
    public static final int tagPay = 667;                                                           //支付
    public static final int tagReceive = 668;                                                       //确认收货
    public static final int tagShopping = 669;                                                      //查看物流
    public static final int tagCustomer = 701;                                                      //联系客服
    public static final int tagReturn = 702;                                                        //退换货
    public static final int tagBuyAgain = 703;                                                      //再次购买

    //数据返回常量key
    public class Response {
        public static final String RESULT = "result";
        public static final String MSG = "msg";
        public static final String STATUS = "status";
        public static final int RESPONSE_CODE_TOEKN_EMPTY = -100;                                   //缺少token
        public static final int RESPONSE_CODE_TOEKN_EXPIRE = -101;                                  //token过期,需要重新登录重新刷新token
        public static final int RESPONSE_CODE_TOEKN_INVALID = -102;                                 //token无效,需要重新登录获取新token
    }

    /*** QQ,微信第三方登录APPID和secret **/
    public static String pluginQQAppid = "1105810390";
    public static String pluginQQSecret = "ZZ2MzAsGY982ZW0V";
    public static String pluginWeixinAppid = "wx9dbcc01b476b14bf";
    public static String pluginWeixinSecret = "7942d154b29b724c6369c230fabf91f7";
    /*** 百度地图相关 **/
    public static String baidumap_mcode = "0D:B5:1A:42:F2:EA:3F:D4:C2:13:BB:0E:31:3B:BF:59:C5:5E:1C:1F;com.tpshop.mall";    //安全码
    public static String baidumap_ak = "wflS14quYqFGzLMmNHdTLfUZgD5xOiat";        //密钥

}
