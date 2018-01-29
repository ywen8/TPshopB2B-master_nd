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
 * $description:  通用工具类
 */
package com.tpshop.mall.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.soubao.tpshop.utils.SPEncryptUtil;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPCommonWebActivity;
import com.tpshop.mall.activity.shop.SPProductDetailActivity_;
import com.tpshop.mall.activity.shop.SPProductListActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.SPHomeBanners;
import com.tpshop.mall.model.shop.SPStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by admin on 2016/7/28
 */
public class SPUtils {

    public static String getHost(String url) {
        if (SPStringUtils.isEmpty(url))
            return null;
        if (url.startsWith("http://") || url.startsWith("https://")) {
            url = url.replaceAll("http://", "").replaceAll("https://", "");
            return url;
        }
        return url;
    }

    /**
     * 网络是否打开
     */
    public static boolean isNetworkAvaiable(Context pContext) {
        boolean isAvaiable = false;
        ConnectivityManager cm = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable())
            isAvaiable = true;
        return isAvaiable;
    }

    public static String convertFullTimeFromPhpTime(long phpTime, String dateFmt) {
        SimpleDateFormat format = new SimpleDateFormat(dateFmt);
        return format.format(new Date(phpTime * 1000));
    }

    public static String convertFullTimeFromPhpTime(long phpTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(phpTime * 1000));
    }

    //当天输出时间,不是输出日期
    public static String getTimeFormPhpTime(long phpTime) {
        Long[] time = getTimeCut(phpTime, System.currentTimeMillis());
        if (time[0] > 0) {
            if (time[0] == 1)
                return "昨天";
            else
                return convertFullTimeFromPhpTime(phpTime, "MM-dd");
        } else {
            return convertFullTimeFromPhpTime(phpTime, "HH:mm");
        }
    }

    public static String getTimeFormPhpTime2(long phpTime) {
        return convertFullTimeFromPhpTime(phpTime, "yyyy-MM-dd");
    }

    public static String getFullTimeFormPhpTime(long phpTime) {
        return convertFullTimeFromPhpTime(phpTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static List<SPStore> convertCollectToListStore(Collection<SPStore> collectStores) {
        if (collectStores == null) return null;
        List<SPStore> storeList = new ArrayList<>();
        Iterator<SPStore> iterator = collectStores.iterator();
        while (iterator.hasNext()) {
            storeList.add(iterator.next());
        }
        return storeList;
    }

    /**
     * 验证一个对象是否是JSONArrray类型
     */
    public static boolean velidateJSONArray(Object object) {
        boolean isJSONArrayType;
        if (object == null || SPStringUtils.isEmpty(object.toString())) {
            isJSONArrayType = false;
        } else {
            try {
                JSONArray jsonArray = (JSONArray) object;
                isJSONArrayType = true;
            } catch (Exception e) {
                isJSONArrayType = false;
            }
        }
        return isJSONArrayType;
    }

    /**
     * 验证一个对象是否是JSONObject类型
     */
    public static boolean velidateJSONObject(Object object) {
        boolean isJSONObject;
        if (object == null || SPStringUtils.isEmpty(object.toString())) {
            isJSONObject = false;
        } else {
            try {
                JSONObject jsonObject = (JSONObject) object;
                isJSONObject = true;
            } catch (Exception e) {
                isJSONObject = false;
            }
        }
        return isJSONObject;
    }

    /**
     * 验证手机号码是否是大陆号码或香港号码
     */
    public static boolean isPhoneLegal(String phone) throws PatternSyntaxException {
        return isChinaPhoneLegal(phone) || isHKPhoneLegal(phone);
    }

    /**
     * 验证手机号码是否是大陆号码
     */
    private static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证手机号码是否是香港手机号码
     */
    private static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 获取两个时间直接的差(毫秒值)
     */
    public static long getTimeCount(long beginTime, long endTime) {
        return endTime * 1000L - beginTime;
    }

    /**
     * 获取两个时间直接的差,然后结果是一个long数组:天、时、分、秒
     */
    public static Long[] getTimeCut(long beginTime, long endTime) {
        Date begin = new Date(beginTime * 1000L);
        Date end = new Date(endTime * 1000L);
        long between = (end.getTime() - begin.getTime()) / 1000;          //除以1000是为了转换成秒
        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;
        long minute = between % 3600 / 60;
        long second = between % 60;
        return new Long[]{day, hour, minute, second};
    }

    /**
     * 密码加密
     */
    public static String md5WithAuthCode(String src) throws Exception {
        String source = SPMobileConstants.SP_AUTH_CODE + src;
        return SPEncryptUtil.md5Digest(source);
    }

    /**
     * 计算文本宽度
     */
    public static int getCharacterWidth(Paint paint, String text, float size) {
        if (null == text || "".equals(text))
            return 0;
        if (paint == null) {
            paint = new Paint();
            paint.setTextSize(size);
        } else {
            paint.setTextSize(size);
        }
        return (int) paint.measureText(text);
    }

    /**
     * 将一个字符串的第一个字符设置指定大小,其他字符不变
     */
    public static SpannableString getFirstCharScale(Context context, String fmtText) {
        float fontSize = context.getResources().getDimension(R.dimen.textSizeNormal);
        return getFirstCharScale(fmtText, Float.valueOf(fontSize).intValue());
    }

    /**
     * 将一个字符串的第一个字符设置指定大小,其他字符不变
     */
    public static SpannableString getFirstCharScale(String fmtText, float fontSize) {
        if (fmtText == null) return null;
        SpannableString spanStr = new SpannableString(fmtText);
        spanStr.setSpan(new AbsoluteSizeSpan(Float.valueOf(fontSize).intValue()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);        //设置前景色为洋红色
        return spanStr;
    }

    public static String getImageUrl(String imagePath) {
        if (SPStringUtils.isEmpty(imagePath)) return null;
        String imageUrl;
        if (imagePath.startsWith("http"))
            imageUrl = imagePath;
        else
            imageUrl = SPMobileConstants.BASE_HOST + imagePath;
        return imageUrl;
    }

    //获取图片Uri
    public static Uri getImageUri(Context context, String imagePath) {
        if (SPStringUtils.isEmpty(imagePath)) return null;
        Uri uri = null;
        if (imagePath.startsWith("http")) {
            uri = Uri.parse(imagePath);
        } else {
            Bitmap mBitmap = BitmapFactory.decodeFile(imagePath);
            if (mBitmap != null)
                uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), mBitmap, null, null));
        }
        return uri;
    }

    /**
     * 生成随机密码
     */
    public static String genRandomNum(int len) {
        final int maxNum = 36;             //35是因为数组是从0开始的,26个字母+10个数字
        int i;         //生成的随机数
        int count = 0;         //生成的密码的长度
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuilder pwd = new StringBuilder();
        Random r = new Random();
        while (count < len) {
            i = Math.abs(r.nextInt(maxNum));            //生成随机数,取绝对值,防止生成负数
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    /**
     * 获取屏幕宽度
     */
    public static int getWindowWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getWindowheight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 隐藏软件盘
     */
    public static void hideSoftInput(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 判断token是否过期
     */
    public static boolean isTokenExpire(int errorCode) {
        return errorCode == SPMobileConstants.Response.RESPONSE_CODE_TOEKN_EXPIRE
                || errorCode == SPMobileConstants.Response.RESPONSE_CODE_TOEKN_INVALID
                || errorCode == SPMobileConstants.Response.RESPONSE_CODE_TOEKN_EMPTY;
    }

    /**
     * 广告跳转
     */
    public static void adTopage(Context context, SPHomeBanners banner) {
        if (banner == null) return;
        Intent intent = null;
        switch (banner.getMediaType()) {
            case 3:           //商品
                intent = new Intent(context, SPProductDetailActivity_.class);
                intent.putExtra("goodsID", banner.getAdLink());
                break;
            case 4:           //商品列表
                intent = new Intent(context, SPProductListActivity.class);
                intent.putExtra("category_id", Integer.parseInt(banner.getAdLink()));
                break;
            case 5:           //网页链接
                intent = new Intent(context, SPCommonWebActivity.class);
                intent.putExtra(SPMobileConstants.KEY_WEB_TITLE, banner.getAdName());
                intent.putExtra(SPMobileConstants.KEY_WEB_URL, banner.getAdLink());
                break;
        }
        if (intent != null)
            context.startActivity(intent);
    }

}
