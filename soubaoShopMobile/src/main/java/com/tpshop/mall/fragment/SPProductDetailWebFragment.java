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
 * Date: @date 2015年10月20日 下午7:19:26
 * Description:MineFragment
 *
 * @version V1.0
 */
package com.tpshop.mall.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.shop.SPProductDetailActivity;
import com.tpshop.mall.common.SPMobileConstants;

/**
 * 商品详情 -> 图文详情
 */
public class SPProductDetailWebFragment extends SPBaseFragment {

    boolean isFirstLoad;
    private WebView mWebView;
    private SPProductDetailActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SPProductDetailActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.common_webview_main, null, false);
        mWebView = (WebView) view.findViewById(R.id.common_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setDisplayZoomControls(false);                //隐藏webview缩放按钮
        mWebView.getSettings().setSupportZoom(false);                        //设定不支持缩放
        mWebView.getSettings().setUseWideViewPort(true);                     //适应屏幕大小
        mWebView.getSettings().setLoadWithOverviewMode(true);
        loadData();
        return view;
    }

    @Override
    public void initSubView(View view) {
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
    }

    public void loadData() {
        if (isFirstLoad && mWebView != null) {
            String url = String.format(SPMobileConstants.URL_GOODS_DETAIL_CONTENT, activity.getProduct().getGoodsID());
            mWebView.loadUrl(url);
            isFirstLoad = false;
        }
    }

}
