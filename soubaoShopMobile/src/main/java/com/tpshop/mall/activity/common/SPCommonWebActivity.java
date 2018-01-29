/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2127 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015-10-15 20:32:41
 * Description: 商品促销活动
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;

import com.tpshop.mall.activity.person.order.SPApplySeriverActivity_;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.model.person.SPUser;
import com.tpshop.mall.widget.ProgressWebView;

/**
 * Created by admin on 2016/7/4
 * 调用该页面需要传递两个参数:请求的URL和activity的标题
 */
public class SPCommonWebActivity extends SPBaseActivity implements View.OnClickListener {

    String mTitle;
    String mWebUrl;
    ProgressWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTitle = getIntent().getStringExtra(SPMobileConstants.KEY_WEB_TITLE);
        mWebUrl = getIntent().getStringExtra(SPMobileConstants.KEY_WEB_URL);
        setCustomerTitle(true, true, mTitle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_main);
        super.initWithWebTitle();
    }

    @Override
    public void initSubViews() {
        mWebView = (ProgressWebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "TPshop");        //设置本地调用对象及其接口
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        RequestParams params = new RequestParams();
        if (SPMobileApplication.getInstance().isLogined()) {
            SPUser user = SPMobileApplication.getInstance().getLoginUser();
            params.put("user_id", user.getUserID());
            if (!SPStringUtils.isEmpty(user.getToken())) params.put("token", user.getToken());
        }
        if (SPMobileApplication.getInstance().getDeviceId() != null) {
            String imei = SPMobileApplication.getInstance().getDeviceId();
            params.put("unique_id", imei);
        }
        int idx = mWebUrl.indexOf("?");
        String connecter = "&";
        if (idx == -1) connecter = "?";
        mWebView.loadUrl(mWebUrl + connecter + params);
        showLoadingSmallToast();
        if (mWebView != null)
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    mLoadingSmallDialog.dismiss();
                }
            });
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
    }

    @android.webkit.JavascriptInterface
    public void apply_return(final int recId) {
        SPPersonRequest.getApplyStatus(recId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                Intent returnIntent = new Intent(SPCommonWebActivity.this, SPApplySeriverActivity_.class);
                returnIntent.putExtra("rec_id", recId + "");
                startActivity(returnIntent);
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    @android.webkit.JavascriptInterface
    public void gotoDistributeStore() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {         //当webview不是处于第一页面时,返回上一个页面
                mWebView.goBack();
                showCloseImageView();
                return true;
            } else {         //当webview处于第一页面时,直接退出程序
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public WebView getWebView() {
        return mWebView;
    }

}
